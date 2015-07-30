package bynull.realty.services.impl.socialnet.fb;

import bynull.realty.common.PhoneUtil;
import bynull.realty.common.Porter;
import bynull.realty.components.AfterCommitExecutor;
import bynull.realty.components.text.RentalFeeParser;
import bynull.realty.components.text.RoomCountParser;
import bynull.realty.components.text.TargetAnalyzer;
import bynull.realty.config.Config;
import bynull.realty.converters.CityModelDTOConverter;
import bynull.realty.converters.FacebookPageModelDTOConverter;
import bynull.realty.converters.FacebookPostModelDTOConverter;
import bynull.realty.converters.MetroModelDTOConverter;
import bynull.realty.dao.ApartmentRepository;
import bynull.realty.dao.MetroRepository;
import bynull.realty.dao.external.ApartmentExternalPhotoRepository;
import bynull.realty.dao.external.FacebookPageToScrapRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.MetroDTO;
import bynull.realty.dto.fb.FacebookPageDTO;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.api.FacebookService;
import bynull.realty.services.impl.CityServiceImpl;
import bynull.realty.services.impl.MetroServiceImpl;
import bynull.realty.services.impl.socialnet.AbstractSocialNetServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;
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
 * Created by dionis on 02/01/15.
 */
@Service
@Slf4j
public class FacebookServiceImpl extends AbstractSocialNetServiceImpl implements FacebookService, InitializingBean {
    @Resource
    ApartmentService apartmentService;

    @Resource
    Config config;
    @Resource
    FacebookPageToScrapRepository facebookPageToScrapRepository;
    @Resource
    ApartmentRepository apartmentRepository;
    @Resource
    FacebookHelperComponent facebookHelperComponent;

    @Resource
    AfterCommitExecutor afterCommitExecutor;

    Porter porter;

    //converters
    @Resource
    FacebookPostModelDTOConverter facebookPostConverter;

    @Resource
    FacebookPageModelDTOConverter facebookPageConverter;

    @Resource
    ApartmentExternalPhotoRepository apartmentExternalPhotoRepository;

    @Resource
    MetroModelDTOConverter metroConverter;

    @PersistenceContext
    EntityManager em;

    @Resource
    MetroRepository metroRepository;

    @Resource
    TransactionOperations transactionOperations;

    RoomCountParser roomCountParser;

    RentalFeeParser rentalFeeParser;

    @Resource
    CityModelDTOConverter cityModelDTOConverter;

    @Resource
    TargetAnalyzer targetAnalyzer;

    @Override
    public void afterPropertiesSet() throws Exception {
        porter = Porter.getInstance();
        roomCountParser = RoomCountParser.getInstance();
        rentalFeeParser = RentalFeeParser.getInstance();
    }

    @Override
    public void syncWithFB() {
        List<FacebookPageToScrap> fbPages = facebookPageToScrapRepository.findAll()
                .stream()
                .filter(FacebookPageToScrap::isEnabled)
                .collect(Collectors.toList());
        List<? extends MetroDTO> metros = transactionOperations.execute(txStatus -> metroConverter.toTargetList(metroRepository.findAll()));

        em.clear();//detach all instances
        AtomicInteger counter = new AtomicInteger();
        Date defaultMaxPostsAgeToGrab = new DateTime().minusDays(30).toDate();
        for (FacebookPageToScrap _fbPage : fbPages) {
            transactionOperations.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    final List<Long> apartmentIdsToPostOnVKPage = new ArrayList<>();
                    FacebookPageToScrap fbPage = facebookPageToScrapRepository.findOne(_fbPage.getId());
                    Optional<CityDTO> cityDTO = cityModelDTOConverter.toTargetType(fbPage.getCity());
                    List<FacebookApartment> newest = apartmentRepository.finFBAparmentsByExternalIdNewest(fbPage.getExternalId(), getLimit1Offset0());

                    Date maxPostsAgeToGrab = newest.isEmpty() ? defaultMaxPostsAgeToGrab : Iterables.getFirst(newest, null).getLogicalCreated();

                    try {
                        List<FacebookHelperComponent.FacebookPostItemDTO> facebookPostItemDTOs = facebookHelperComponent.loadPostsFromPage(fbPage.getExternalId(), maxPostsAgeToGrab)
                                .stream()
                                .filter(item -> StringUtils.trimToNull(item.getMessage()) != null && !StringUtils.trimToEmpty(item.getMessage()).contains("rent4.me"))
                                        //leave only those that have no duplicates in DB
//                                .filter(item -> apartmentRepository.countOfSimilarApartments(item.getMessage()) == 0)
                                .collect(Collectors.toCollection(ArrayList::new));

                        List<FacebookApartment> byExternalIdIn = !facebookPostItemDTOs.isEmpty() ? apartmentRepository.findFBApartmentsByExternalIdIn(facebookPostItemDTOs.stream()
                                        .map(FacebookHelperComponent.FacebookPostItemDTO::getId)
                                        .collect(Collectors.toList())
                        ) : Collections.emptyList();

                        Set<String> collect = facebookPostItemDTOs.stream().map(item -> Apartment.calcHash(item.getMessage())).collect(Collectors.toSet());

                        final Set<String> similarTextsInDB = apartmentRepository.similarApartments(collect);

                        Set<String> ids = byExternalIdIn.stream()
                                .map(FacebookApartment::getExternalId)
                                .collect(Collectors.toSet());

                        //although it may seems strange but in same result set could be returned duplicates - so filter them
                        log.info("Removing duplicates in same requests by id");
                        Set<String> postItemDtoIds = new HashSet<>();
                        Set<String> postItemDtoContents = new HashSet<>();
                        Iterator<FacebookHelperComponent.FacebookPostItemDTO> iterator = facebookPostItemDTOs.iterator();
                        while (iterator.hasNext()) {
                            FacebookHelperComponent.FacebookPostItemDTO next = iterator.next();
                            if (next.getId() == null || postItemDtoIds.contains(next.getId()) || postItemDtoContents.contains(next.getMessage())) {
                                log.info("Removed duplicate in same requests by id: [{}]", next.getId());
                                iterator.remove();
                                continue;
                            } else {
                                postItemDtoIds.add(next.getId());
                                postItemDtoContents.add(next.getMessage());
                            }
                        }
                        log.info("Removed duplicates in same requests by id");

                        log.info("Removing duplicates in DB by id");
                        List<FacebookHelperComponent.FacebookPostItemDTO> facebookPostItemDTOsToPersist = facebookPostItemDTOs
                                .stream()
                                .filter(i -> !ids.contains(i.getId()) && !similarTextsInDB.contains(i.getMessage()))
                                .collect(Collectors.toList());

                        log.info("Removed duplicates in DB by id");


                        for (FacebookHelperComponent.FacebookPostItemDTO postItemDTO : facebookPostItemDTOsToPersist) {
                            FacebookApartment post = new FacebookApartment();

                            int i = counter.incrementAndGet();
                            log.info(">>> Processing post #[{}]", i);
                            //fill
                            post.setExternalId(postItemDTO.getId());
                            post.setLink(postItemDTO.getExternalLink());
                            post.setExtAuthorLink(postItemDTO.getExternalAuthorLink());
                            post.setDescription(postItemDTO.getMessage());
                            post.setPublished(true);

                            post.setLogicalCreated(postItemDTO.getCreatedDtime());


                            post.setFacebookPage(fbPage);
                            String message = post.getDescription();

                            Set<MetroEntity> matchedMetros = matchMetros(metros, message, cityDTO);
                            post.setMetros(matchedMetros);

                            GeoPoint averagePoint = getAveragePoint(matchedMetros);

                            if (averagePoint != null) {
                                post.setLocation(averagePoint);
                            } else {
                                post.setLocation(getAveragePoint(cityDTO));
                            }

                            AddressComponents addressComponents = new AddressComponents();
                            addressComponents.setCountryCode("RU");
                            post.setAddressComponents(addressComponents);

                            Integer roomCount = roomCountParser.findRoomCount(message);
                            post.setRoomCount(roomCount);
                            Apartment.Target target = targetAnalyzer.determineTarget(message);
                            post.setTarget(target);
                            BigDecimal rentalFee = rentalFeeParser.findRentalFee(message);
                            post.setRentalFee(rentalFee);
                            post.setFeePeriod(FeePeriod.MONTHLY);
                            List<PhoneUtil.Phone> phones = PhoneUtil.findPhoneNumbers(message, "RU");
                            Set<Contact> contacts = phones.stream().map(phone -> {
                                PhoneContact contact = new PhoneContact();
                                contact.setPhoneNumber(PhoneNumber.from(phone));
                                return contact;
                            }).collect(Collectors.toCollection(HashSet::new));
                            post.setContacts(contacts);
                            post = apartmentRepository.save(post);

                            String picture = StringUtils.trimToNull(postItemDTO.getPicture());
                            if (picture != null) {
                                ApartmentExternalPhoto photo = new ApartmentExternalPhoto();
                                photo.setImageUrl(picture);
                                photo.setPreviewUrl(picture);
                                photo.setApartment(post);
                                apartmentExternalPhotoRepository.save(photo);
                            }

                            post = apartmentRepository.save(post);
                            if (post.getCity() != null && MetroServiceImpl.MOSCOW_CITY_DESCRIPTION.getCity().equalsIgnoreCase(post.getCity().getName())
                                    && !StringUtils.trimToEmpty(post.getDescription()).contains("rent4.me")) {

                                int scorePoints = 0;

                                if (post.getRentalFee() != null) {
                                    scorePoints++;
                                }
                                if (post.getMetros() != null && !post.getMetros().isEmpty()) {
                                    scorePoints++;
                                }
                                if (post.getRoomCount() != null) {
                                    scorePoints++;
                                }

                                log.info("Score points for FB post #[{}]: [{}]", post.getId(), scorePoints);

                                if (scorePoints >= 1) {
                                    apartmentIdsToPostOnVKPage.add(post.getId());
                                }
                            }
                            log.info("<<< Processing of post #[{}] done", i);

//                                post.setType(getType().toInternal());
                        }
                        em.flush();
                        log.info("Saved [{}] posts for vk page: [{}]", facebookPostItemDTOsToPersist.size(), fbPage.getLink());
                    } catch (Exception e) {
                        log.error("Failed to parse [" + fbPage.getExternalId() + "]", e);
                    }
                    afterCommitExecutor.executeAsynchronouslyInTransaction(() -> {
                        apartmentService.publishApartmentsOnOurVkGroupPage(apartmentIdsToPostOnVKPage);
                    });
                }
            });

        }
    }

    private PageRequest getLimit1Offset0() {
        return new PageRequest(0, 1);
    }


    @Transactional
    @Override
    public void save(FacebookPageDTO pageDTO) {
        FacebookPageToScrap entity = facebookPageConverter.toSourceType(pageDTO);
        if (entity.getId() != null) {
            FacebookPageToScrap found = facebookPageToScrapRepository.findOne(entity.getId());
            found.setLink(entity.getLink());
            found.setExternalId(entity.getExternalId());
            found.setEnabled(entity.isEnabled());
            found.setCity(entity.getCity() != null && entity.getCity().getId() != null ? entity.getCity() : null);
            facebookPageToScrapRepository.saveAndFlush(found);
        } else {
            facebookPageToScrapRepository.saveAndFlush(entity);
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        facebookPageToScrapRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacebookPageDTO> listAllPages() {
        return facebookPageToScrapRepository.findAll()
                .stream()
                .sorted((o1, o2) -> o2.getUpdated().compareTo(o1.getUpdated()))
                .map(f -> facebookPageConverter.toTargetType(Optional.of(f)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<FacebookPageDTO> findPageById(long fbPageId) {
        return facebookPageConverter.toTargetType(
                Optional.ofNullable(facebookPageToScrapRepository.findOne(fbPageId))
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<FacebookPostDTO> findPosts(String text, PageRequest pageRequest) {
        String txt = ("%" + text + "%").toLowerCase();
        //TODO: fix it later.
//        List<FacebookScrapedPost> byQuery = text != null ? facebookScrapedPostRepository.findByQuery(txt, pageRequest) : facebookScrapedPostRepository.findAll(pageRequest).getContent();
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    @Override
    public long countOfPages() {
        //TODO: fix it later
        return 0;/*facebookScrapedPostRepository.count();*/
    }

    @Transactional
    @Override
    public void reparseExistingFBPosts() {
        List<? extends MetroDTO> metros = metroConverter.toTargetList(metroRepository.findAll());
        int countOfMatchedPosts = 0;
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "id");
        Page<FacebookApartment> postsPage = apartmentRepository.findFBAll(pageable);
        long total = apartmentRepository.countFB();
        boolean hasNext = false;
        do {
            List<FacebookApartment> posts = postsPage.getContent();
            for (FacebookApartment post : posts) {
                String message = post.getDescription();

                Optional<CityDTO> cityDTO = cityModelDTOConverter.toTargetType(post.getFacebookPage().getCity());

                Set<MetroEntity> matchedMetros = matchMetros(metros, message, cityDTO);
                post.setMetros(matchedMetros);

                GeoPoint averagePoint = getAveragePoint(matchedMetros);
                if (averagePoint != null) {
                    post.setLocation(averagePoint);
                } else if (cityDTO != null) {
                    post.setLocation(getAveragePoint(cityDTO));
                } else {
                    post.setLocation(null);
                }

                AddressComponents addressComponents = new AddressComponents();
                addressComponents.setCountryCode("RU");
                post.setAddressComponents(addressComponents);

                Integer roomCount = roomCountParser.findRoomCount(message);
                post.setRoomCount(roomCount);
                BigDecimal rentalFee = rentalFeeParser.findRentalFee(message);
                post.setRentalFee(rentalFee);
                Apartment.Target target = targetAnalyzer.determineTarget(message);
                post.setTarget(target);
                List<PhoneUtil.Phone> phones = PhoneUtil.findPhoneNumbers(message, "RU");
                Set<Contact> contacts = phones.stream().map(phone -> {
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
                postsPage = apartmentRepository.findFBAll(pageable);
            }
        } while (hasNext);
        em.flush();
        log.info("Total count of matched posts to metro stations: [{}]. Total posts: [{}]", countOfMatchedPosts, total);
    }

    @Override
    public long countByQuery(String text) {
        String query = ("%" + text + "%").toLowerCase();
        return 0;
        //TODO: fix it later
        //return text != null ? facebookScrapedPostRepository.countByQuery(query) : facebookScrapedPostRepository.count();
    }

}
