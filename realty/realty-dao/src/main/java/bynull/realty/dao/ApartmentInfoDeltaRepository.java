package bynull.realty.dao;

import bynull.realty.data.business.ApartmentInfoDelta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author dionis on 08/12/14.
 */
public interface ApartmentInfoDeltaRepository extends JpaRepository<ApartmentInfoDelta, Long> {
    @Query("select delta from ApartmentInfoDelta delta where apartment.id=:apartment_id order by created desc")
    List<ApartmentInfoDelta> findLatestForApartment(@Param("apartment_id") Long id);

    @Modifying
    @Query("update ApartmentInfoDelta d set d.applied=true where d IN (select dl from ApartmentInfoDelta dl where dl.updated <= :dt)")
    void applyAllDeltasUntilDateIncludingSpecified(@Param("dt")Date timestamp);

    @Modifying
    @Query("update ApartmentInfoDelta d set d.rejected=true where d IN (select dl from ApartmentInfoDelta dl where dl.updated <= :dt)")
    void rejectAllDeltasUntilDateIncludingSpecified(@Param("dt")Date timestamp);

    @Query(value = "SELECT distinct on (apartment_id) * from apartment_deltas ORDER BY apartment_id, created_dt DESC;", nativeQuery = true)
    List<ApartmentInfoDelta> listAllGroupedByApartments();
}
