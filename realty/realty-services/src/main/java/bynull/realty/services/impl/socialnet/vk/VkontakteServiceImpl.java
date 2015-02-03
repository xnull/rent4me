package bynull.realty.services.impl.socialnet.vk;

import bynull.realty.components.VKHelperComponent;
import bynull.realty.components.text.MetroTextAnalyzer;
import bynull.realty.components.text.RentalFeeParser;
import bynull.realty.components.text.RoomCountParser;
import bynull.realty.converters.MetroModelDTOConverter;
import bynull.realty.converters.VkontaktePageModelDTOConverter;
import bynull.realty.converters.VkontaktePostModelDTOConverter;
import bynull.realty.dao.MetroRepository;
import bynull.realty.dao.external.VkontaktePageRepository;
import bynull.realty.dao.external.VkontaktePostRepository;
import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import bynull.realty.data.business.external.vkontakte.VkontaktePost;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.dto.MetroDTO;
import bynull.realty.dto.vk.VkontaktePageDTO;
import bynull.realty.dto.vk.VkontaktePostDTO;
import bynull.realty.services.api.VkontakteService;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dionis on 28/01/15.
 */
@Slf4j
@Service
public class VkontakteServiceImpl implements VkontakteService, InitializingBean {

    @Resource
    VkontaktePageRepository vkontaktePageRepository;

    @Resource
    VkontaktePageModelDTOConverter vkontaktePageConverter;

    @Resource
    MetroRepository metroRepository;

    @Resource
    MetroModelDTOConverter metroConverter;

    @Resource
    VkontaktePostRepository vkontaktePostRepository;

    @Resource
    VKHelperComponent vkHelperComponent;


    RoomCountParser roomCountParser;


    RentalFeeParser rentalFeeParser;

    @Resource
    MetroTextAnalyzer metroTextAnalyzer;

    @Resource
    VkontaktePostModelDTOConverter vkPostConverter;

    @PersistenceContext
    EntityManager em;

    @Override
    public void afterPropertiesSet() throws Exception {
        roomCountParser = RoomCountParser.getInstance();
        rentalFeeParser = RentalFeeParser.getInstance();
    }

    @Transactional
    @Override
    public void syncWithVK() {
        List<VkontaktePage> fbPages = vkontaktePageRepository.findAll();
        List<MetroDTO> metros = metroConverter.toTargetList(metroRepository.findAll());

        em.clear();//detach all instances
        Date defaultMaxPostsAgeToGrab = new DateTime().minusDays(30).toDate();
        for (VkontaktePage vkPage : fbPages) {
            List<VkontaktePost> newest = vkontaktePostRepository.findByExternalIdNewest(vkPage.getExternalId(), getLimit1Offset0());

            Date maxPostsAgeToGrab = newest.isEmpty() ? defaultMaxPostsAgeToGrab : Iterables.getFirst(newest, null).getCreated();

            try {
                List<VKHelperComponent.VkWallPostDTO> postItemDTOs = vkHelperComponent.loadPostsFromPage(vkPage.getExternalId(), maxPostsAgeToGrab);
                List<VkontaktePost> byExternalIdIn = !postItemDTOs.isEmpty() ? vkontaktePostRepository.findByExternalIdIn(postItemDTOs.stream()
                                .map(VKHelperComponent.VkWallPostDTO::getId)
                                .collect(Collectors.toList())
                ) : Collections.emptyList();
                Set<String> ids = byExternalIdIn.stream()
                        .map(VkontaktePost::getExternalId)
                        .collect(Collectors.toSet());

                List<VKHelperComponent.VkWallPostDTO> dtosToPersist = postItemDTOs
                        .stream()
                        .filter(i -> !ids.contains(i.getId()))
                        .collect(Collectors.toList());

                em.clear();

                final Date threeMonthsAgo = new DateTime().minusMonths(3).toDate();

                Iterable<List<VKHelperComponent.VkWallPostDTO>> partitions = Iterables.partition(dtosToPersist, 20);
                for (List<VKHelperComponent.VkWallPostDTO> partition : partitions) {
                    VkontaktePage page = new VkontaktePage(vkPage.getId());
                    for (VKHelperComponent.VkWallPostDTO postItemDTO : partition) {
                        VkontaktePost post = postItemDTO.toInternal();
                        if (post.getCreated() != null && post.getCreated().before(threeMonthsAgo)) {
                            log.info("Skipping post that's older than 3 months old: [{}]");
                            continue;
                        }
                        post.setVkontaktePage(page);
                        String message = post.getMessage();
                        Set<MetroEntity> matchedMetros = matchMetros(metros, message);
                        post.setMetros(matchedMetros);
                        Integer roomCount = roomCountParser.findRoomCount(message);
                        post.setRoomCount(roomCount);
                        BigDecimal rentalFee = rentalFeeParser.findRentalFee(message);
                        post.setRentalFee(rentalFee);
                        vkontaktePostRepository.save(post);
                    }
                    em.flush();
                    em.clear();
                }
                em.flush();
            } catch (Exception e) {
                log.error("Failed to parse [" + vkPage.getExternalId() + "]", e);
            }
        }
    }

    private Set<MetroEntity> matchMetros(List<MetroDTO> metros, String message) {
        Set<MetroEntity> matchedMetros = new HashSet<>();
        for (MetroDTO metro : metros) {
            if (metroTextAnalyzer.matches(message, metro.getStationName())) {
//                log.info("Post #matched to metro #[] ({})", metro.getId(), metro.getStationName());

                matchedMetros.add(metroRepository.findOne(metro.getId()));

            }
        }
        return matchedMetros;
    }

    private PageRequest getLimit1Offset0() {
        return new PageRequest(0, 1);
    }

    @Transactional(readOnly = true)
    @Override
    public List<VkontaktePageDTO> listAllPages() {
        return vkontaktePageConverter.toTargetList(vkontaktePageRepository.findAll(new Sort(Sort.Direction.DESC, "updated")));
    }

    @Transactional
    @Override
    public void delete(long vkPageId) {
        vkontaktePageRepository.delete(vkPageId);
    }

    @Transactional
    @Override
    public void save(VkontaktePageDTO vkontaktePageDTO) {
        VkontaktePage one = vkontaktePageDTO.getId() != null ? vkontaktePageRepository.findOne(vkontaktePageDTO.getId()) : new VkontaktePage();

        one.setLink(vkontaktePageDTO.getLink());
        one.setExternalId(vkontaktePageDTO.getExternalId());
        vkontaktePageRepository.saveAndFlush(one);
    }

    @Transactional(readOnly = true)
    @Override
    public VkontaktePageDTO findPageById(long fbPageId) {
        return vkontaktePageConverter.toTargetType(vkontaktePageRepository.findOne(fbPageId));
    }

    @Transactional(readOnly = true)
    @Override
    public long countByQuery(String text) {
        String query = ("%" + text + "%").toLowerCase();
        return text != null ? vkontaktePostRepository.countByQuery(query) : vkontaktePostRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public List<VkontaktePostDTO> findPosts(String text, PageRequest pageRequest) {
        String txt = ("%" + text + "%").toLowerCase();
        List<VkontaktePost> byQuery = text != null ? vkontaktePostRepository.findByQuery(txt, pageRequest) : vkontaktePostRepository.findAll(pageRequest).getContent();
        return vkPostConverter.toTargetList(byQuery);
    }
}
