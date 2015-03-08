package bynull.realty.services.impl.socialnet.vk;

import bynull.realty.common.PhoneUtil;
import bynull.realty.components.VKHelperComponent;
import bynull.realty.components.text.MetroTextAnalyzer;
import bynull.realty.components.text.RentalFeeParser;
import bynull.realty.components.text.RoomCountParser;
import bynull.realty.components.text.TargetAnalyzer;
import bynull.realty.converters.MetroModelDTOConverter;
import bynull.realty.converters.VkontaktePageModelDTOConverter;
import bynull.realty.converters.VkontaktePostModelDTOConverter;
import bynull.realty.dao.ApartmentRepository;
import bynull.realty.dao.MetroRepository;
import bynull.realty.dao.external.VkontaktePageRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.MetroDTO;
import bynull.realty.dto.vk.VkontaktePageDTO;
import bynull.realty.dto.vk.VkontaktePostDTO;
import bynull.realty.services.api.VkontakteService;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.concurrent.atomic.AtomicInteger;
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
    ApartmentRepository apartmentRepository;

    @Resource
    VKHelperComponent vkHelperComponent;


    RoomCountParser roomCountParser;


    RentalFeeParser rentalFeeParser;

    @Resource
    MetroTextAnalyzer metroTextAnalyzer;

    @Resource
    TargetAnalyzer targetAnalyzer;

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
        List<? extends MetroDTO> metros = metroConverter.toTargetList(metroRepository.findAll());

        em.clear();//detach all instances
        AtomicInteger counter = new AtomicInteger();
        Date defaultMaxPostsAgeToGrab = new DateTime().minusDays(30).toDate();
        for (VkontaktePage _vkPage : vkPages) {
            transactionOperations.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    VkontaktePage vkPage = vkontaktePageRepository.findOne(_vkPage.getId());
                    List<VkontakteApartment> newest = apartmentRepository.findVkAparmentsByExternalIdNewest(vkPage.getExternalId(), getLimit1Offset0());

                    Date maxPostsAgeToGrab = newest.isEmpty() ? defaultMaxPostsAgeToGrab : Iterables.getFirst(newest, null).getCreated();

                    try {
                        List<VKHelperComponent.VkWallPostDTO> postItemDTOs = vkHelperComponent.loadPostsFromPage(vkPage.getExternalId(), maxPostsAgeToGrab)
                                .stream()
                                .filter(item -> StringUtils.trimToNull(item.getText()) != null)
                                //leave only those that have no duplicates in DB
                                .collect(Collectors.toCollection(ArrayList::new));
                        List<VkontakteApartment> byExternalIdIn = !postItemDTOs.isEmpty() ? apartmentRepository.findVkApartmentsByExternalIdIn(postItemDTOs.stream()
                                        .map(VKHelperComponent.VkWallPostDTO::getId)
                                        .collect(Collectors.toList())
                        ) : Collections.emptyList();

                        Set<String> collect = postItemDTOs.stream().map(item -> Apartment.calcHash(item.getText())).collect(Collectors.toSet());

                        final Set<String> similarTextsInDB = apartmentRepository.similarApartments(collect);

                        Set<String> ids = byExternalIdIn.stream()
                                .map(VkontakteApartment::getExternalId)
                                .collect(Collectors.toSet());

                        //although it may seems strange but in same result set could be returned duplicates - so filter them
                        log.info("Removing duplicates in same requests by id");
                        Set<String> postItemDtoIds = new HashSet<>();
                        Set<String> postItemDtoContents = new HashSet<>();
                        Iterator<VKHelperComponent.VkWallPostDTO> iterator = postItemDTOs.iterator();
                        while (iterator.hasNext()) {
                            VKHelperComponent.VkWallPostDTO next = iterator.next();
                            if (next.getId() == null || postItemDtoIds.contains(next.getId()) || postItemDtoContents.contains(next.getText())) {
                                log.info("Removed duplicate in same requests by id or text: [{}], [{}]", next.getId(), next.getText());
                                iterator.remove();
                                continue;
                            } else {
                                postItemDtoIds.add(next.getId());
                                postItemDtoContents.add(next.getText());
                            }
                        }
                        log.info("Removed duplicates in same requests by id/texts");

                        log.info("Removing duplicates in DB by id");
                        List<VKHelperComponent.VkWallPostDTO> dtosToPersist = postItemDTOs
                                .stream()
                                .filter(i -> !ids.contains(i.getId()) && !similarTextsInDB.contains(i.getText()))
                                .collect(Collectors.toList());

                        log.info("Removed duplicates in DB by id");


                        for (VKHelperComponent.VkWallPostDTO postItemDTO : dtosToPersist) {
                            int i = counter.incrementAndGet();
                            log.info(">>> Processing post #[{}]", i);
                            VkontakteApartment post = postItemDTO.toInternal();
                            post.setVkontaktePage(vkPage);
                            post.setPublished(true);
                            String message = post.getDescription();
                            Set<MetroEntity> matchedMetros = matchMetros(metros, message);
                            post.setMetros(matchedMetros);
                            Integer roomCount = roomCountParser.findRoomCount(message);
                            post.setRoomCount(roomCount);
                            BigDecimal rentalFee = rentalFeeParser.findRentalFee(message);
                            post.setRentalFee(rentalFee);
                            Apartment.Target target = targetAnalyzer.determineTarget(message);
                            post.setTarget(target);
                            post.setFeePeriod(FeePeriod.MONTHLY);
                            List<PhoneUtil.Phone> phones = PhoneUtil.findPhoneNumbers(message, "RU");
                            Set<Contact> contacts =  phones.stream().map(phone -> {
                                PhoneContact contact = new PhoneContact();
                                contact.setPhoneNumber(PhoneNumber.from(phone));
                                return contact;
                            }).collect(Collectors.toCollection(HashSet::new));
                            post.setContacts(contacts);
                            apartmentRepository.save(post);
                            log.info("<<< Processing of post #[{}] done", i);
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

    private Set<MetroEntity> matchMetros(List<? extends MetroDTO> metros, String message) {
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
    public List<? extends VkontaktePageDTO> listAllPages() {
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
        return text != null ? apartmentRepository.countVkByQuery(query) : apartmentRepository.countVK();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ApartmentDTO> findPosts(String text, PageRequest pageRequest) {
        String txt = ("%" + text + "%").toLowerCase();
        List<VkontakteApartment> byQuery = text != null ? apartmentRepository.findVkByQuery(txt, pageRequest) : apartmentRepository.findVKAll(pageRequest).getContent();
        //TODO: fix converter later
        return Collections.emptyList();
    }

    @Transactional
    @Override
    public void reparseExistingVKPosts() {
        List<? extends MetroDTO> metros = metroConverter.toTargetList(metroRepository.findAll());
        int countOfMatchedPosts = 0;
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "id");
        Page<VkontakteApartment> postsPage = apartmentRepository.findVKAll(pageable);
        long total = apartmentRepository.countVK();
        boolean hasNext = false;
        do {
            List<VkontakteApartment> posts = postsPage.getContent();
            for (VkontakteApartment post : posts) {
                String message = post.getDescription();

                Set<MetroEntity> matchedMetros = matchMetros(metros, message);
                post.setMetros(matchedMetros);
                Integer roomCount = roomCountParser.findRoomCount(message);
                post.setRoomCount(roomCount);
                BigDecimal rentalFee = rentalFeeParser.findRentalFee(message);
                post.setRentalFee(rentalFee);
                Apartment.Target target = targetAnalyzer.determineTarget(message);
                post.setTarget(target);

                List<PhoneUtil.Phone> phones = PhoneUtil.findPhoneNumbers(message, "RU");
                Set<Contact> contacts =  phones.stream().map(phone -> {
                    PhoneContact contact = new PhoneContact();
                    contact.setPhoneNumber(PhoneNumber.from(phone));
                    return contact;
                }).collect(Collectors.toCollection(HashSet::new));
                post.setContacts(contacts);

                if (!matchedMetros.isEmpty()) {
                    countOfMatchedPosts++;
                }
                post = apartmentRepository.save(post);
            }
            log.info("Processed page #[{}]", pageable);
            hasNext = postsPage.hasNext();
            if (hasNext) {
                pageable = postsPage.nextPageable();
                em.flush();
                em.clear();
                postsPage = apartmentRepository.findVKAll(pageable);
            }
        } while (hasNext);
        em.flush();
        log.info("Total count of matched posts to metro stations: [{}]. Total posts: [{}]", countOfMatchedPosts, total);
    }
}
