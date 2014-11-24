package bynull.realty.services.api;

import bynull.realty.dto.ApartmentDTO;

import java.util.List;

/**
 * @author dionis on 22/06/14.
 */
public interface ApartmentService {
    ApartmentDTO create(ApartmentDTO dto);

    boolean createForAuthorizedUser(ApartmentDTO dto);

    void update(ApartmentDTO dto);

    ApartmentDTO find(Long id);

    void delete(Long id);

    List<ApartmentDTO> findAll();

    ApartmentDTO findAuthorizedUserApartment();

    void deleteApartmentForAuthorizedUser();
}
