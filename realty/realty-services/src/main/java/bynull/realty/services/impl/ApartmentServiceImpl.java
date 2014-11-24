package bynull.realty.services.impl;

import bynull.realty.dao.ApartmentRepository;
import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.User;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.services.api.ApartmentService;
import bynull.realty.utils.SecurityUtils;
import com.google.common.collect.Iterables;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author dionis on 22/06/14.
 */
@Service
public class ApartmentServiceImpl implements ApartmentService {
    @Resource
    ApartmentRepository apartmentRepository;

    @Resource
    UserRepository userRepository;

    @Transactional
    @Override
    public ApartmentDTO create(ApartmentDTO dto) {

        Apartment apartment = dto.toInternal();

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
            boolean result = user.getApartments().add(apartment);
            return result;
        } else {
            return false;
        }
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
    public void delete(Long id) {
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
            apartmentRepository.delete(apartments);
        }
    }
}
