package bynull.realty.dao;

import bynull.realty.data.business.ApartmentLocationDelta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author dionis on 08/12/14.
 */
public interface ApartmentLocationDeltaRepository extends JpaRepository<ApartmentLocationDelta, Long> {
    @Query("select delta from ApartmentLocationDelta delta where apartment.id=:apartment_id order by created desc")
    List<ApartmentLocationDelta> findLatestForApartment(@Param("apartment_id")Long id);
}
