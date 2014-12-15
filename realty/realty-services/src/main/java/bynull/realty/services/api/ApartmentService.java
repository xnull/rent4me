package bynull.realty.services.api;

import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.util.LimitAndOffset;

import java.util.List;

/**
 * @author dionis on 22/06/14.
 */
public interface ApartmentService {
    ApartmentDTO create(ApartmentDTO dto);

    boolean createForAuthorizedUser(ApartmentDTO dto);

    void update(ApartmentDTO dto);

    ApartmentDTO find(Long id);

    void delete(long id);

    List<ApartmentDTO> findAll();

    ApartmentDTO findAuthorizedUserApartment();

    void deleteApartmentForAuthorizedUser();

    boolean updateForAuthorizedUser(ApartmentDTO dto);

    void applyLatestApartmentInfoDeltaForApartment(ApartmentDTO dto);

    void requestApartmentInfoChangeForAuthorizedUser(ApartmentDTO dto);

    List<ApartmentDTO> findNearestForCountry(GeoPoint geoPoint, String countryCode, LimitAndOffset limitAndOffset);
}
