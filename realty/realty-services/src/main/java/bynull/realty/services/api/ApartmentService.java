package bynull.realty.services.api;

import bynull.realty.dao.ApartmentRepository;
import bynull.realty.dao.ApartmentRepositoryCustom;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.util.LimitAndOffset;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

/**
 * @author dionis on 22/06/14.
 */
public interface ApartmentService {
    ApartmentDTO create(ApartmentDTO dto);

    boolean createForAuthorizedUser(ApartmentDTO dto);

    void update(ApartmentDTO dto);

    ApartmentDTO find(Long id);

    void delete(long id);

    ApartmentDTO findAuthorizedUserApartment();

    void deleteApartmentForAuthorizedUser();

    boolean updateForAuthorizedUser(ApartmentDTO dto);

    void applyLatestApartmentInfoDeltaForApartment(ApartmentDTO dto);

    void requestApartmentInfoChangeForAuthorizedUser(ApartmentDTO dto);

    List<ApartmentDTO> findNearestForCountry(GeoPoint geoPoint, String countryCode, Double latLow, Double lngLow, Double latHigh, Double lngHigh, LimitAndOffset limitAndOffset);

    List<ApartmentDTO> findPosts(String text, boolean withSubway, Set<ApartmentRepository.RoomCount> roomsCount, Integer minPrice, Integer maxPrice, ApartmentRepository.FindMode findMode, ApartmentRepositoryCustom.GeoParams geoParams, List<Long> metroIds, LimitAndOffset limitAndOffset);

    List<? extends ApartmentDTO> listAll(PageRequest pageRequest);

    void showApartmentInSearch(long id);

    void hideApartmentFromSearch(long id);

    void unPublishOldNonInternalApartments();
}
