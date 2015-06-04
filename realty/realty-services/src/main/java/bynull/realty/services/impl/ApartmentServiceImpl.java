package bynull.realty.services.impl;

import bynull.realty.components.VKHelperComponent;
import bynull.realty.converters.apartments.ApartmentModelDTOConverter;
import bynull.realty.converters.apartments.ApartmentModelDTOConverterFactory;
import bynull.realty.dao.*;
import bynull.realty.data.business.*;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ApartmentPhotoDTO;
import bynull.realty.services.api.ApartmentPhotoService;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.utils.SecurityUtils;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
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

        InternalApartment apartment = new InternalApartment();
        apartment.mergeWith(dto.toInternal());

        InternalApartment created = apartmentRepository.saveAndFlush(apartment);

        ApartmentModelDTOConverter<Apartment> targetConverter = apartmentModelDTOConverterFactory.getTargetConverter(created);
        return targetConverter.toTargetType(created);
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

            boolean result = user.getApartments().add(apartment);
            return result;
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
    public ApartmentDTO find(Long id) {
        Apartment one = apartmentRepository.findOne(id);
        ApartmentModelDTOConverter<Apartment> targetConverter = apartmentModelDTOConverterFactory.getTargetConverter(one);

        return targetConverter.toTargetType(one);
    }

    @Transactional
    @Override
    public void delete(long id) {
        apartmentRepository.delete(id);
        apartmentRepository.flush();
    }

    @Transactional(readOnly = true)
    @Override
    public ApartmentDTO findAuthorizedUserApartment() {
        User user = getAuthorizedUser();
        if (user != null) {
            Apartment first = Iterables.getFirst(user.getApartments(), null);
            ApartmentModelDTOConverter<Apartment> targetConverter = apartmentModelDTOConverterFactory.getTargetConverter(first);
            return targetConverter.toTargetType(first);
        } else {
            return null;
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
                .map(apartment -> {
                    ApartmentModelDTOConverter<Apartment> targetConverter = apartmentModelDTOConverterFactory.getTargetConverter(apartment);
                    return targetConverter.toTargetType(apartment);
                })
                .collect(Collectors.toList())
                ;
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

        return posts.stream().map(e -> {
            ApartmentModelDTOConverter<Apartment> targetConverter = apartmentModelDTOConverterFactory.getTargetConverter(e);
            return targetConverter.toTargetType(e);
        }).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    @Override
    public List<? extends ApartmentDTO> findSimilarToApartment(long apartmentId) {
        List<Apartment> similarApartments = apartmentRepository.findSimilarApartments(apartmentId);

        return similarApartments.stream().map(e -> {
            ApartmentModelDTOConverter<Apartment> targetConverter = apartmentModelDTOConverterFactory.getTargetConverter(e);
            return targetConverter.toTargetType(e);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<? extends ApartmentDTO> listAll(PageRequest pageRequest) {
        Page<Apartment> apartments = apartmentRepository.findAll(pageRequest);

        return apartments.getContent()
                .stream()
                .map(apartment -> {
                    ApartmentModelDTOConverter<Apartment> targetConverter = apartmentModelDTOConverterFactory.getTargetConverter(apartment);
                    return targetConverter.toTargetType(apartment);
                })
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

    @Transactional
    @Override
    public void publishFBApartmentsOnVkPage(Date startDate, Date endDate) {
        List<FacebookApartment> apartments = apartmentRepository.findFBApartmentsSinceTime(startDate, endDate);
        long size = apartments.size();
        log.info("Apartments found: [{}]", size);

        apartments = apartments.stream().filter(this::adShouldBePosted).collect(Collectors.toList());

        log.info("Apartments published: [{}]", apartments.size());

        String groupId="-82219356";//public group
        String pageId="-95509841";//public page

        for (FacebookApartment apartment : apartments) {
            String desc = toDescription(apartment);
            long start = System.currentTimeMillis();
            vkHelperComponent.sendMessageToGroup(pageId, desc);
            long end = System.currentTimeMillis();
            long diff = end - start;
            //wait for 30-60 seconds in order to fool vk if they will decide to ban us
            long minWaitThreshold = 30000+(System.currentTimeMillis()%30000);
            if(diff < minWaitThreshold) {
                long sleepForMs = minWaitThreshold - diff;
                log.info("Waiting [{}] ms before next publishing on FB", sleepForMs);
                Uninterruptibles.sleepUninterruptibly(sleepForMs, TimeUnit.MILLISECONDS);
            }
        }

    }

    private boolean adShouldBePosted(Apartment apartment) {
        if(apartment.getMetros() != null && !apartment.getMetros().isEmpty()) {
            return true;
        }

        if(apartment.getRoomCount() != null) {
            return true;
        }

        if(apartment.getRentalFee() != null) {
            return true;
        }

        return false;
    }

    private String toDescription(Apartment apartment) {
        String desc = StringUtils.trimToEmpty(apartment.getDescription());
        if(desc.length() > 256) {
            desc = StringUtils.substring(desc, 0, 256)+"...\nhttp://rent4.me/advert/"+apartment.getId();
        }
        return desc;
    }
}
