package bynull.realty.services.impl;

import bynull.realty.dao.*;
import bynull.realty.data.business.*;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ApartmentPhotoDTO;
import bynull.realty.services.api.ApartmentPhotoService;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.utils.CoordinateUtils;
import bynull.realty.utils.SecurityUtils;
import com.google.common.collect.Iterables;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author dionis on 22/06/14.
 */
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
    ApartmentLocationDeltaRepository apartmentLocationDeltaRepository;

    @Transactional
    @Override
    public ApartmentDTO create(ApartmentDTO dto) {

        Apartment apartment = new Apartment();
        apartment.mergeWith(dto.toInternal());

        Apartment created = apartmentRepository.saveAndFlush(apartment);

        return ApartmentDTO.from(created);
    }

    @Transactional
    @Override
    public boolean createForAuthorizedUser(ApartmentDTO dto) {
        User user = getAuthorizedUser();

        if(user.getApartments().isEmpty()) {
            Apartment apartment = dto.toInternal();
            apartment.setOwner(user);
            apartment = apartmentRepository.saveAndFlush(apartment);

            handlePhotoDiff(dto, apartment);

            boolean result = user.getApartments().add(apartment);
            return result;
        } else {
            return false;
        }
    }

    private void handlePhotoDiff(ApartmentDTO dto, Apartment apartment) {
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

        Apartment apartment = apartmentRepository.findOne(dto.getId());

        Assert.notNull(apartment);

        SecurityUtils.verifySameUser(apartment.getOwner());

        GeoPoint currentLocation = apartment.getLocation();

        Apartment updatedApartment = dto.toInternal();


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
        return ApartmentDTO.from(apartmentRepository.findOne(id));
    }

    @Transactional
    @Override
    public void delete(long id) {
        apartmentRepository.delete(id);
        apartmentRepository.flush();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ApartmentDTO> findAll() {
        List<Apartment> all = apartmentRepository.findAll();
        List<ApartmentDTO> result = new ArrayList<ApartmentDTO>(all.size());
        for (Apartment apartment : all) {
            result.add(ApartmentDTO.from(apartment));
        }
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public ApartmentDTO findAuthorizedUserApartment() {
        User user = getAuthorizedUser();
        return user != null
                ? ApartmentDTO.from(Iterables.getFirst(user.getApartments(), null))
                : null;
    }

    private User getAuthorizedUser() {
        return userRepository.getOne(SecurityUtils.getAuthorizedUser().getId());
    }

    @Transactional
    @Override
    public void deleteApartmentForAuthorizedUser() {
        User user = getAuthorizedUser();
        Set<Apartment> apartments = user.getApartments();
        if(!apartments.isEmpty()) {
            for (Apartment apartment : apartments) {
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
    public void applyLatestLocationInfoDeltaForApartment(ApartmentDTO dto) {
        Assert.notNull(dto);
        Assert.notNull(dto.getId());
        Apartment apartment = apartmentRepository.findOne(dto.getId());
        Assert.notNull(apartment);
        List<ApartmentLocationDelta> deltas = apartmentLocationDeltaRepository.findLatestForApartment(dto.getId());
        ApartmentLocationDelta delta = Iterables.getFirst(deltas, null);
        if (delta != null && !delta.isApplied()) {
            apartment.setAddressComponents(delta.getAddressComponents());
            apartment.setLocation(delta.getLocation());
            apartment = apartmentRepository.saveAndFlush(apartment);

            delta.setApplied(true);
            delta = apartmentLocationDeltaRepository.saveAndFlush(delta);
        }
    }
}
