package bynull.realty.services.impl.socialnet.fb;

import bynull.realty.common.PhoneUtil;
import bynull.realty.components.text.MetroTextAnalyzer;
import bynull.realty.common.Porter;
import bynull.realty.components.text.RentalFeeParser;
import bynull.realty.components.text.RoomCountParser;
import bynull.realty.config.Config;
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
import bynull.realty.dto.MetroDTO;
import bynull.realty.dto.fb.FacebookPageDTO;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.services.api.FacebookService;
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
import java.util.stream.Collectors;

/**
 * Created by dionis on 02/01/15.
 */
@Service
@Slf4j
public class FacebookServiceImpl implements FacebookService, InitializingBean {
    private final HttpClient httpManager = new HttpClient(new MultiThreadedHttpConnectionManager()) {{

        final HttpClientParams params = new HttpClientParams();
        params.setIntParameter(HttpClientParams.MAX_REDIRECTS, 5);
        //wait for 3 seconds max to obtain connection
        params.setConnectionManagerTimeout(3 * 1000);
        params.setSoTimeout(10 * 1000);

        this.setParams(params);
    }};
    private final ObjectMapper jacksonObjectMapper = new ObjectMapper();
    @Resource
    Config config;
    @Resource
    FacebookPageToScrapRepository facebookPageToScrapRepository;
    @Resource
    ApartmentRepository apartmentRepository;
    @Resource
    FacebookHelperComponent facebookHelperComponent;

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
    MetroTextAnalyzer metroTextAnalyzer;

    @Resource
    TransactionOperations transactionOperations;

    RoomCountParser roomCountParser;

    RentalFeeParser rentalFeeParser;

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
        List<MetroDTO> metros = metroConverter.toTargetList(metroRepository.findAll());

        em.clear();//detach all instances
        Date defaultMaxPostsAgeToGrab = new DateTime().minusDays(30).toDate();
        for (FacebookPageToScrap _fbPage : fbPages) {
            transactionOperations.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    FacebookPageToScrap fbPage = facebookPageToScrapRepository.findOne(_fbPage.getId());
                    List<FacebookApartment> newest = apartmentRepository.finFBAparmentsByExternalIdNewest(fbPage.getExternalId(), getLimit1Offset0());

                    Date maxPostsAgeToGrab = newest.isEmpty() ? defaultMaxPostsAgeToGrab : Iterables.getFirst(newest, null).getCreated();

                    try {
                        List<FacebookHelperComponent.FacebookPostItemDTO> facebookPostItemDTOs = facebookHelperComponent.loadPostsFromPage(fbPage.getExternalId(), maxPostsAgeToGrab);
                        List<FacebookApartment> byExternalIdIn = !facebookPostItemDTOs.isEmpty() ? apartmentRepository.findFBApartmentsByExternalIdIn(facebookPostItemDTOs.stream()
                                        .map(FacebookHelperComponent.FacebookPostItemDTO::getId)
                                        .collect(Collectors.toList())
                        ) : Collections.emptyList();
                        Set<String> ids = byExternalIdIn.stream()
                                .map(FacebookApartment::getExternalId)
                                .collect(Collectors.toSet());

                        //although it may seems strange but in same result set could be returned duplicates - so filter them
                        log.info("Removing duplicates in same requests by id");
                        Set<String> postItemDtoIds = new HashSet<>();
                        Iterator<FacebookHelperComponent.FacebookPostItemDTO> iterator = facebookPostItemDTOs.iterator();
                        while (iterator.hasNext()) {
                            FacebookHelperComponent.FacebookPostItemDTO next = iterator.next();
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
                        List<FacebookHelperComponent.FacebookPostItemDTO> facebookPostItemDTOsToPersist = facebookPostItemDTOs
                                .stream()
                                .filter(i -> !ids.contains(i.getId()))
                                .collect(Collectors.toList());

                        log.info("Removed duplicates in DB by id");


                        for (FacebookHelperComponent.FacebookPostItemDTO postItemDTO : facebookPostItemDTOsToPersist) {
                            FacebookApartment post = new FacebookApartment();


                            //fill
                            post.setExternalId(postItemDTO.getId());
                            post.setLink(postItemDTO.getLink());
                            post.setDescription(postItemDTO.getMessage());

                            post.setLogicalCreated(postItemDTO.getCreatedDtime());



                            post.setFacebookPage(fbPage);
                            String message = post.getDescription();
                            Set<MetroEntity> matchedMetros = matchMetros(metros, message);
                            post.setMetros(matchedMetros);
                            Integer roomCount = roomCountParser.findRoomCount(message);
                            post.setRoomCount(roomCount);
                            BigDecimal rentalFee = rentalFeeParser.findRentalFee(message);
                            post.setRentalFee(rentalFee);
                            post.setFeePeriod(FeePeriod.MONTHLY);
                            List<PhoneUtil.Phone> phones = PhoneUtil.findPhoneNumbers(message, "RU");
                            Set<Contact> contacts =  phones.stream().map(phone -> {
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
                                photo.setApartment(post);
                                apartmentExternalPhotoRepository.save(photo);
                            }

                            apartmentRepository.save(post);

//                                post.setType(getType().toInternal());
                        }
                        em.flush();
                        log.info("Saved [{}] posts for vk page: [{}]", facebookPostItemDTOsToPersist.size(), fbPage.getLink());
                    } catch (Exception e) {
                        log.error("Failed to parse [" + fbPage.getExternalId() + "]", e);
                    }
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
                .sorted(new Comparator<FacebookPageToScrap>() {
                    @Override
                    public int compare(FacebookPageToScrap o1, FacebookPageToScrap o2) {
                        return o2.getUpdated().compareTo(o1.getUpdated());
                    }
                })
                .map(facebookPageConverter::toTargetType)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public FacebookPageDTO findPageById(long fbPageId) {
        return facebookPageConverter.toTargetType(facebookPageToScrapRepository.findOne(fbPageId));
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
        List<MetroDTO> metros = metroConverter.toTargetList(metroRepository.findAll());
        int countOfMatchedPosts = 0;
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "id");
        Page<FacebookApartment> postsPage = apartmentRepository.findFBAll(pageable);
        long total = apartmentRepository.countFB();
        boolean hasNext = false;
        do {
            List<FacebookApartment> posts = postsPage.getContent();
            for (FacebookApartment post : posts) {
                String message = post.getDescription();

                Set<MetroEntity> matchedMetros = matchMetros(metros, message);
                post.setMetros(matchedMetros);
                Integer roomCount = roomCountParser.findRoomCount(message);
                post.setRoomCount(roomCount);
                BigDecimal rentalFee = rentalFeeParser.findRentalFee(message);
                post.setRentalFee(rentalFee);
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
                postsPage = apartmentRepository.findFBAll(pageable);
            }
        } while (hasNext);
        em.flush();
        log.info("Total count of matched posts to metro stations: [{}]. Total posts: [{}]", countOfMatchedPosts, total);
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

    @Override
    public long countByQuery(String text) {
        String query = ("%" + text + "%").toLowerCase();
        return 0;
        //TODO: fix it later
        //return text != null ? facebookScrapedPostRepository.countByQuery(query) : facebookScrapedPostRepository.count();
    }

}
