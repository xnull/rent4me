package bynull.realty.services.impl;

import bynull.realty.components.VKHelperComponent;
import bynull.realty.converters.apartments.ApartmentModelDTOConverter;
import bynull.realty.converters.apartments.ApartmentModelDTOConverterFactory;
import bynull.realty.dao.*;
import bynull.realty.data.business.*;
import bynull.realty.data.business.vk.stats.VkPublishingEvent;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ApartmentPhotoDTO;
import bynull.realty.services.api.ApartmentPhotoService;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.services.api.VkPublishingEventService;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.utils.HibernateUtil;
import bynull.realty.utils.SecurityUtils;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author dionis on 22/06/14.
 */
@Slf4j
@Service
public class ApartmentServiceImpl implements ApartmentService {
    @Resource
    ApartmentRepository apartmentRepository;

    @Resource
    VkPublishingEventService vkPublishingEventService;

    @Resource
    UserRepository userRepository;

    @Resource
    ApartmentPhotoService apartmentPhotoService;

    @Resource
    ApartmentPhotoRepository apartmentPhotoRepository;

    @Resource
    PhotoTempRepository photoTempRepository;

    @Resource
    ApartmentInfoDeltaRepository apartmentInfoDeltaRepository;

    @Resource
    ApartmentModelDTOConverterFactory<Apartment> apartmentModelDTOConverterFactory;

    @Resource
    VKHelperComponent vkHelperComponent;

    @Transactional
    @Override
    public ApartmentDTO create(ApartmentDTO dto) {
        log.trace("Create apartment");

        InternalApartment apartment = new InternalApartment();
        apartment.mergeWith(dto.toInternal());

        InternalApartment created = apartmentRepository.saveAndFlush(apartment);

        ApartmentModelDTOConverter<Apartment> targetConverter = apartmentModelDTOConverterFactory.getTargetConverter(created);
        return targetConverter.toTargetType(Optional.ofNullable(created)).orElse(null);
    }

    @Transactional
    @Override
    public boolean createForAuthorizedUser(ApartmentDTO dto) {
        User user = getAuthorizedUser();

        if (user.getApartments().isEmpty()) {
            InternalApartment apartment = dto.toInternal();
            apartment.setOwner(user);
            apartment.setPublished(true);//publish by default
            apartment.setTarget(Apartment.Target.RENTER);
            apartment = apartmentRepository.saveAndFlush(apartment);

            handlePhotoDiff(dto, apartment);

            return user.getApartments().add(apartment);
        } else {
            return false;
        }
    }

    private void handlePhotoDiff(ApartmentDTO dto, InternalApartment apartment) {
        List<String> addedTempPhotoGUIDs = dto.getAddedTempPhotoGUIDs();

        //find added photo temps
        List<PhotoTemp> photoTempByGUIDs = !addedTempPhotoGUIDs.isEmpty()
                ? photoTempRepository.findByGuidIn(addedTempPhotoGUIDs)
                : Collections.emptyList();


        for (PhotoTemp photoTemp : photoTempByGUIDs) {
            ApartmentPhotoDTO apartmentPhotoWithThumbnails = apartmentPhotoService.createApartmentPhotoWithThumbnails(photoTemp);
            apartment.addApartmentPhoto(apartmentPhotoRepository.findOne(apartmentPhotoWithThumbnails.getId()));
        }

        List<String> deletePhotoGUIDs = dto.getDeletePhotoGUIDs();
        List<ApartmentPhotoDTO> photosToDelete = apartmentPhotoService.findPlaceProfilePhotosByGUIDs(deletePhotoGUIDs);
        apartmentPhotoService.deleteAll(photosToDelete);
    }

    @Transactional
    @Override
    public boolean updateForAuthorizedUser(ApartmentDTO dto) {
        Assert.notNull(dto);
        Assert.notNull(dto.getId());

        InternalApartment apartment = (InternalApartment) apartmentRepository.findOne(dto.getId());

        Assert.notNull(apartment);

        SecurityUtils.verifySameUser(apartment.getOwner());

        GeoPoint currentLocation = apartment.getLocation();

        InternalApartment updatedApartment = dto.toInternal();


        apartment.mergeWithRentInfoOnly(updatedApartment);


        handlePhotoDiff(dto, apartment);

        apartment = apartmentRepository.saveAndFlush(apartment);

        return true;
    }

    @Transactional
    @Override
    public void update(ApartmentDTO dto) {
        Apartment apartment = apartmentRepository.findOne(dto.getId());
        if (apartment == null) {
            throw new IllegalArgumentException("Not found");
        }
        apartment.updateFrom(dto.toInternal());
        apartmentRepository.saveAndFlush(apartment);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ApartmentDTO> find(Long id) {
        log.debug("Find apartment: {}", id);
        Apartment apartment = HibernateUtil.deproxy(apartmentRepository.findOne(id));
        if (apartment != null) {
            return apartmentModelDTOConverterFactory.getTargetConverter(apartment).toTargetType(Optional.of(apartment));
        }

        log.debug("Apartment not found: {}", id);
        return Optional.empty();
    }

    @Transactional
    @Override
    public void delete(long id) {
        apartmentRepository.delete(id);
        apartmentRepository.flush();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ApartmentDTO> findAuthorizedUserApartment() {
        User user = getAuthorizedUser();
        if (user != null) {
            Apartment first = Iterables.getFirst(user.getApartments(), null);
            ApartmentModelDTOConverter<Apartment> targetConverter = apartmentModelDTOConverterFactory.getTargetConverter(first);
            return targetConverter.toTargetType(Optional.ofNullable(first));
        } else {
            return Optional.empty();
        }
    }

    private User getAuthorizedUser() {
        return userRepository.getOne(SecurityUtils.getAuthorizedUser().getId());
    }

    @Transactional
    @Override
    public void deleteApartmentForAuthorizedUser() {
        User user = getAuthorizedUser();
        Set<InternalApartment> apartments = user.getApartments();
        if (!apartments.isEmpty()) {
            for (InternalApartment apartment : apartments) {
                SecurityUtils.verifySameUser(apartment.getOwner());
                List<ApartmentPhoto> apartmentPhotos = apartment.listPhotosNewestFirst();

                //delete all photos first
                apartmentPhotoService.deleteAll(
                        apartmentPhotos.stream()
                                .map(ApartmentPhotoDTO::from)
                                .collect(Collectors.toList())
                );
            }
            apartmentRepository.delete(apartments);
        }
    }

    @Transactional
    @Override
    public void applyLatestApartmentInfoDeltaForApartment(ApartmentDTO dto) {
        Assert.notNull(dto);
        Assert.notNull(dto.getId());
        Apartment apartment = apartmentRepository.findOne(dto.getId());
        Assert.notNull(apartment);
        List<ApartmentInfoDelta> deltas = apartmentInfoDeltaRepository.findLatestForApartment(dto.getId());
        ApartmentInfoDelta delta = Iterables.getFirst(deltas, null);
        if (delta != null && !delta.isApplied()) {
            apartment.setAddressComponents(delta.getAddressComponents());
            apartment.setLocation(delta.getLocation());
            apartment.setArea(delta.getArea());
            apartment.setFloorNumber(delta.getFloorNumber());
            apartment.setFloorsTotal(delta.getFloorsTotal());
            apartment.setRoomCount(delta.getRoomCount());
            apartment = apartmentRepository.saveAndFlush(apartment);

            delta.setApplied(true);
            delta = apartmentInfoDeltaRepository.saveAndFlush(delta);
        }
    }

    @Transactional
    @Override
    public void requestApartmentInfoChangeForAuthorizedUser(ApartmentDTO dto) {
        Assert.notNull(dto);
        Assert.notNull(dto.getId());

        InternalApartment apartment = (InternalApartment) apartmentRepository.findOne(dto.getId());
        SecurityUtils.verifySameUser(apartment.getOwner());

        ApartmentInfoDelta delta = dto.toApartmentInfoDelta();
        delta.setApartment(apartment);

        delta = apartmentInfoDeltaRepository.saveAndFlush(delta);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ApartmentDTO> findNearestForCountry(GeoPoint geoPoint, String countryCode, Double latLow, Double lngLow, Double latHigh, Double lngHigh, LimitAndOffset limitAndOffset) {
        Assert.notNull(geoPoint);
        Assert.notNull(countryCode);
        Assert.notNull(limitAndOffset);

        boolean boundingBoxSpecified = latLow != null && lngLow != null && latHigh != null && lngHigh != null;

        final List<Apartment> result;

        if (boundingBoxSpecified) {
            result = apartmentRepository.findNearestInBoundingBox(
                    geoPoint.getLongitude(),
                    geoPoint.getLatitude(),
                    countryCode,
                    latLow,
                    lngLow,
                    latHigh,
                    lngHigh,
                    limitAndOffset.limit,
                    limitAndOffset.offset
            );
        } else {
            result = apartmentRepository.findNearest(
                    geoPoint.getLongitude(),
                    geoPoint.getLatitude(),
                    countryCode,
                    limitAndOffset.limit,
                    limitAndOffset.offset
            );
        }
        //TODO: change to generic implementation
        return result.stream()
                .map(it -> (InternalApartment) it)
                .map(apartment -> apartmentModelDTOConverterFactory.getTargetConverter(apartment).toTargetType(Optional.ofNullable(apartment)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ApartmentDTO> findPosts(String text, boolean withSubway, Set<ApartmentRepository.RoomCount> roomsCount, Integer minPrice, Integer maxPrice, ApartmentRepository.FindMode findMode, ApartmentRepositoryCustom.GeoParams geoParams, List<Long> metroIds, LimitAndOffset limitAndOffset) {
        Assert.notNull(text);
        Assert.notNull(roomsCount);
        Assert.notNull(geoParams);
        Assert.notNull(metroIds);

        Assert.notNull(roomsCount);
        text = StringUtils.trimToEmpty(text);


        List<Apartment> posts = apartmentRepository.findPosts(text, withSubway, roomsCount, minPrice, maxPrice, findMode, geoParams, metroIds, limitAndOffset);

        return posts.stream()
                .map(e -> apartmentModelDTOConverterFactory.getTargetConverter(e).toTargetType(Optional.ofNullable(e)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    @Override
    public List<? extends ApartmentDTO> findSimilarToApartment(long apartmentId) {
        List<Apartment> similarApartments = apartmentRepository.findSimilarApartments(apartmentId);

        return similarApartments.stream()
                .map(e -> apartmentModelDTOConverterFactory.getTargetConverter(e).toTargetType(Optional.ofNullable(e)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<? extends ApartmentDTO> listAll(PageRequest pageRequest) {
        log.trace("List all apartments");

        Page<Apartment> apartments = apartmentRepository.findAll(pageRequest);

        return apartments.getContent()
                .stream()
                .map(apartment ->
                                apartmentModelDTOConverterFactory.getTargetConverter(apartment)
                                        .toTargetType(Optional.ofNullable(apartment))
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void showApartmentInSearch(long id) {
        Apartment apartment = apartmentRepository.findOne(id);
        apartment.setPublished(true);
        apartment = apartmentRepository.saveAndFlush(apartment);
    }

    @Transactional
    @Override
    public void hideApartmentFromSearch(long id) {
        Apartment apartment = apartmentRepository.findOne(id);
        apartment.setPublished(false);
        apartment = apartmentRepository.saveAndFlush(apartment);
    }

    @Transactional
    @Override
    public void unPublishOldNonInternalApartments() {
        apartmentRepository.unPublishOldNonInternalApartments(new DateTime().minusDays(31).toDate());
    }

    private boolean adShouldBePosted(Apartment apartment) {
        if (apartment.getMetros() != null && !apartment.getMetros().isEmpty()) {
            return true;
        }

        if (apartment.getRoomCount() != null) {
            return true;
        }

        if (apartment.getRentalFee() != null) {
            return true;
        }

        return false;
    }

    private String toDescription(Apartment apartment) {
        String desc = StringUtils.trimToEmpty(apartment.getDescription());
        if (desc.length() > 256) {
            desc = StringUtils.substring(desc, 0, 256) + "...";
        }

        desc += "\n\n" +
                "http://rent4.me/advert/" + apartment.getId();
        return desc;
    }

    @Transactional
    @Override
    public void publishApartmentsOnOurVkGroupPage(List<Long> apartmentIds) {
        if (apartmentIds.isEmpty()) return;
        List<Apartment> apartments = apartmentRepository.findByIdIn(apartmentIds);
        long size = apartments.size();
        log.info("Apartments found: [{}]", size);

        apartments = apartments.stream().filter(this::adShouldBePosted).collect(Collectors.toList());

        log.info("Apartments published: [{}]", apartments.size());

        String groupId = "-82219356";//public group - old
        groupId = "-95509841";//public group - new

        log.info("CrossPostPublishingToVK: Getting max count");
        long maxCountOfPublishingMessagesDuringPeriod = vkHelperComponent.getMaxCountOfPublishingMessagesDuringPeriod();
        log.info("CrossPostPublishingToVK: Got max count: [{}]", maxCountOfPublishingMessagesDuringPeriod);

        publishing_cycle:
        for (Apartment apartment : apartments) {
            apartment = HibernateUtil.deproxy(apartment);
            if (apartment instanceof SocialNetApartment) {
                final Apartment.DataSource dataSource;
                if (apartment instanceof VkontakteApartment) {
                    log.info("CrossPostPublishingToVK: Getting published VK events");
                    long count = vkPublishingEventService.countOfPublishedEventsWithDataSource(Apartment.DataSource.VKONTAKTE, vkHelperComponent.getTokenRestrictionPeriod());
                    log.info("CrossPostPublishingToVK: Got published VK events: [{}]", count);
                    dataSource = Apartment.DataSource.VKONTAKTE;
                    //set it to 40% for vk
                    long maxAllowedMessagesPublishedDuringPeriod = maxCountOfPublishingMessagesDuringPeriod * 40 / 100;
                    if (count >= maxAllowedMessagesPublishedDuringPeriod) {
                        log.info("CrossPostPublishingToVK: Skipping VK - more than needed");
                        continue publishing_cycle;
                    }
                } else if (apartment instanceof FacebookApartment) {
                    log.info("CrossPostPublishingToVK: Getting published FB events");
                    long count = vkPublishingEventService.countOfPublishedEventsWithDataSource(Apartment.DataSource.FACEBOOK, vkHelperComponent.getTokenRestrictionPeriod());
                    log.info("CrossPostPublishingToVK: Got published FB events: [{}]", count);
                    dataSource = Apartment.DataSource.FACEBOOK;
                    //set it to 60% for fb
                    long maxAllowedMessagesPublishedDuringPeriod = maxCountOfPublishingMessagesDuringPeriod * 50 / 100;
                    if (count >= maxAllowedMessagesPublishedDuringPeriod) {
                        log.info("CrossPostPublishingToVK: Skipping FB - more than needed");
                        continue publishing_cycle;
                    }
                } else {
                    log.info("CrossPostPublishingToVK: Unsupported class provided for publishing: [{}]. Skipping.", apartment.getClass());
                    log.warn("CrossPostPublishingToVK: Unsupported class provided for publishing: [{}]. Skipping.", apartment.getClass());
                    continue publishing_cycle;
                }

                String desc = toDescription(apartment);
                long start = System.currentTimeMillis();
                Optional<String> token = vkHelperComponent.grabToken();
                if (!token.isPresent()) {
                    log.info("CrossPostPublishingToVK: No token present. Stopping publishing cycle.");
                    log.error("CrossPostPublishingToVK: No token present. Stopping publishing cycle.");
                    break publishing_cycle;
                }

                String accessToken = token.get();
                log.info("CrossPostPublishingToVK: Publishing stats");
                vkPublishingEventService.publishEvent(dataSource, groupId, desc, accessToken);
                log.info("CrossPostPublishingToVK: Sending message to vk");
                vkHelperComponent.sendMessageToGroup(accessToken, groupId, desc);
                log.info("CrossPostPublishingToVK: Sent message to vk");
                long end = System.currentTimeMillis();
                long diff = end - start;
                //wait for 30-60 seconds in order to fool vk if they will decide to ban us
                long minWaitThreshold = 30000 + (System.currentTimeMillis() % 30000);
                if (diff < minWaitThreshold) {
                    long sleepForMs = minWaitThreshold - diff;
                    log.info("Waiting [{}] ms before next publishing on FB", sleepForMs);
                    Uninterruptibles.sleepUninterruptibly(sleepForMs, TimeUnit.MILLISECONDS);
                }
            } else {
                log.info("Apartment of not supported class provided: " + apartment.getClass());
            }
        }
    }
}
