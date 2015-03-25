package bynull.realty.services.api;

import bynull.realty.dto.ApartmentInfoDeltaDTO;

import java.util.List;
import java.util.Optional;

/**
 * Created by dionis on 3/25/15.
 */
public interface ApartmentInfoDeltaService {
    Optional<ApartmentInfoDeltaDTO> findById(Long id);

    void applyAllDeltasUntilDateIncludingSpecified(Long id);

    void rejectAllDeltasUntilDateIncludingSpecified(Long id);

    List<? extends ApartmentInfoDeltaDTO> listAllGroupedByApartments();
}
