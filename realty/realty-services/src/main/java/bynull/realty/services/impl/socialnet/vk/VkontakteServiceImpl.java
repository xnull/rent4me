package bynull.realty.services.impl.socialnet.vk;

import bynull.realty.common.PhoneUtil;
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
import bynull.realty.data.business.PhoneNumber;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionOperations;

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

    @Resource
    TransactionOperations transactionOperations;

    @Override
    public void afterPropertiesSet() throws Exception {
        roomCountParser = RoomCountParser.getInstance();
        rentalFeeParser = RentalFeeParser.getInstance();
    }

//    @Transactional
    @Override
    public void syncWithVK() {
        List<VkontaktePage> vkPages = vkontaktePageRepository.findAll()
                                                .stream()
                                                .filter(VkontaktePage::isEnabled)
                                                .collect(Collectors.toList());
        List<MetroDTO> metros = metroConverter.toTargetList(metroRepository.findAll());

        em.clear();//detach all instances
        Date defaultMaxPostsAgeToGrab = new DateTime().minusDays(30).toDate();
        for (VkontaktePage _vkPage : vkPages) {
            transactionOperations.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    VkontaktePage vkPage = vkontaktePageRepository.findOne(_vkPage.getId());
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

                        //although it may seems strange but in same result set could be returned duplicates - so filter them
                        log.info("Removing duplicates in same requests by id");
                        Set<String> postItemDtoIds = new HashSet<>();
                        Iterator<VKHelperComponent.VkWallPostDTO> iterator = postItemDTOs.iterator();
                        while (iterator.hasNext()) {
                            VKHelperComponent.VkWallPostDTO next = iterator.next();
                            if(next.getId() == null || postItemDtoIds.contains(next.getId())){
                                log.info("Removed duplicate in same requests by id: [{}]", next.getId());
                                iterator.remove();
                                continue;
                            } else {
                                postItemDtoIds.add(next.getId());
                            }
                        }
                        log.info("Removed duplicates in same requests by id");

                        log.info("Removing duplicates in DB by id");
                        List<VKHelperComponent.VkWallPostDTO> dtosToPersist = postItemDTOs
                                .stream()
                                .filter(i -> !ids.contains(i.getId()))
                                .collect(Collectors.toList());

                        log.info("Removed duplicates in DB by id");

                        for (VKHelperComponent.VkWallPostDTO postItemDTO : dtosToPersist) {
                            VkontaktePost post = postItemDTO.toInternal();
                            post.setVkontaktePage(vkPage);
                            String message = post.getMessage();
                            Set<MetroEntity> matchedMetros = matchMetros(metros, message);
                            post.setMetros(matchedMetros);
                            Integer roomCount = roomCountParser.findRoomCount(message);
                            post.setRoomCount(roomCount);
                            BigDecimal rentalFee = rentalFeeParser.findRentalFee(message);
                            post.setRentalFee(rentalFee);
                            PhoneUtil.Phone phone = PhoneUtil.findFirstPhoneNumber(message, "RU");
                            post.setPhoneNumber(PhoneNumber.from(phone));
                            vkontaktePostRepository.save(post);
                        }
                        em.flush();
                        log.info("Saved [{}] posts for vk page: [{}]", dtosToPersist.size(), vkPage.getLink());
                    } catch (Exception e) {
                        log.error("Failed to parse [" + vkPage.getExternalId() + "]", e);
                    }
                }
            });
        }
    }

    private Set<MetroEntity> matchMetros(List<MetroDTO> metros, String message) {
        log.info(">> Matching metros started");
        try {
            Set<MetroEntity> matchedMetros = new HashSet<>();
            for (MetroDTO metro : metros) {
                if (metroTextAnalyzer.matches(message, metro.getStationName())) {
    //                log.info("Post #matched to metro #[] ({})", metro.getId(), metro.getStationName());

                    matchedMetros.add(metroRepository.findOne(metro.getId()));

                }
            }
            return matchedMetros;
        } finally {
            log.info("<< Matching metros ended");
        }
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
        one.setEnabled(vkontaktePageDTO.isEnabled());
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

    @Transactional
    @Override
    public void reparseExistingVKPosts() {
        List<MetroDTO> metros = metroConverter.toTargetList(metroRepository.findAll());
        int countOfMatchedPosts = 0;
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "id");
        Page<VkontaktePost> postsPage = vkontaktePostRepository.findAll(pageable);
        long total = vkontaktePostRepository.count();
        boolean hasNext = false;
        do {
            List<VkontaktePost> posts = postsPage.getContent();
            for (VkontaktePost post : posts) {
                String message = post.getMessage();

                Set<MetroEntity> matchedMetros = matchMetros(metros, message);
                post.setMetros(matchedMetros);
                Integer roomCount = roomCountParser.findRoomCount(message);
                post.setRoomCount(roomCount);
                BigDecimal rentalFee = rentalFeeParser.findRentalFee(message);
                post.setRentalFee(rentalFee);

                PhoneUtil.Phone phone = PhoneUtil.findFirstPhoneNumber(message, "RU");
                post.setPhoneNumber(PhoneNumber.from(phone));

                if (!matchedMetros.isEmpty()) {
                    countOfMatchedPosts++;
                }
                post = vkontaktePostRepository.save(post);
            }
            log.info("Processed page #[{}]", pageable);
            hasNext = postsPage.hasNext();
            if (hasNext) {
                pageable = postsPage.nextPageable();
                em.flush();
                em.clear();
                postsPage = vkontaktePostRepository.findAll(pageable);
            }
        } while (hasNext);
        em.flush();
        log.info("Total count of matched posts to metro stations: [{}]. Total posts: [{}]", countOfMatchedPosts, total);
    }
}
