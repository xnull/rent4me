package bynull.realty.dao;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.common.GeoPoint;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author dionis on 22/06/14.
 */
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    @Query(value = "select a.* from apartments a where a.published=true and upper(a.country_code)=upper(:countryCode) order by a.location <-> ST_GeomFromText( concat('SRID=4326;POINT(',:lng,' ',:lat,')') ) limit :limit offset :offset", nativeQuery = true)
    List<Apartment> findNearest(@Param("lng")double lng, @Param("lat") double lat, @Param("countryCode") String countryCode, @Param("limit") int limit, @Param("offset") int offset);
}
