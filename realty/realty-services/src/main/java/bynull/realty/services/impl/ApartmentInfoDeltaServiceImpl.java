package bynull.realty.services.impl;

import bynull.realty.converters.apartments.ApartmentInfoDeltaModelDTOConverter;
import bynull.realty.dao.ApartmentInfoDeltaRepository;
import bynull.realty.dao.ApartmentRepository;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.ApartmentInfoDelta;
import bynull.realty.dto.ApartmentInfoDeltaDTO;
import bynull.realty.services.api.ApartmentInfoDeltaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by dionis on 3/25/15.
 */
@Service
public class ApartmentInfoDeltaServiceImpl implements ApartmentInfoDeltaService {
    @Resource
    ApartmentInfoDeltaRepository apartmentInfoDeltaRepository;

    @Resource
    ApartmentRepository apartmentRepository;

    @Resource
    ApartmentInfoDeltaModelDTOConverter converter;

    @Transactional(readOnly = true)
    @Override
    public Optional<ApartmentInfoDeltaDTO> findById(Long id) {
        ApartmentInfoDelta one = apartmentInfoDeltaRepository.findOne(id);
        return Optional.ofNullable(converter.toTargetType(one));
    }

    @Transactional
    @Override
    public void applyAllDeltasUntilDateIncludingSpecified(Long id) {
        ApartmentInfoDelta one = apartmentInfoDeltaRepository.findOne(id);
        Apartment apartment = one.getApartment();
        apartment.applyDelta(one);
        apartment = apartmentRepository.saveAndFlush(apartment);
        apartmentInfoDeltaRepository.applyAllDeltasUntilDateIncludingSpecified(one.getCreated());
    }

    @Transactional
    @Override
    public void rejectAllDeltasUntilDateIncludingSpecified(Long id) {
        ApartmentInfoDelta one = apartmentInfoDeltaRepository.findOne(id);

        apartmentInfoDeltaRepository.rejectAllDeltasUntilDateIncludingSpecified(one.getCreated());
    }

    @Transactional(readOnly = true)
    @Override
    public List<? extends ApartmentInfoDeltaDTO> listAllGroupedByApartments(Set<ListMode> listModes) {
        List<ApartmentInfoDelta> deltas = apartmentInfoDeltaRepository.listAllGroupedByApartments(listModes.contains(ListMode.APPLIED), listModes.contains(ListMode.REJECTED));

        return converter.toTargetList(deltas);
    }
}
