package bynull.realty.dao;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.FacebookApartment;
import bynull.realty.data.business.VkontakteApartment;
import bynull.realty.data.common.GeoPoint;
import org.springframework.data.domain.Page;
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
    @Query(value = "select a.* from apartments a " +
            "where a.published=true " +
            "and upper(a.country_code)=upper(:countryCode) " +
            //check here that it's in bounding box
            "and st_setsrid(st_makebox2d(ST_GeomFromText( concat('SRID=4326;POINT('," +
                ":lng_low," +
                "' '," +
                ":lat_low," +
            "')')), ST_GeomFromText( concat('SRID=4326;POINT('," +
            ":lng_high," +
            "' '," +
            ":lat_high," +
            "')'))), 4326)" +
            " ~ a.location" +
            " " +
            "order by a.location <-> ST_GeomFromText( concat('SRID=4326;POINT(',:lng,' ',:lat,')') ) limit :limit offset :offset", nativeQuery = true)
    List<Apartment> findNearestInBoundingBox(
            @Param("lng") double lng,
            @Param("lat") double lat,
            @Param("countryCode") String countryCode,
            @Param("lat_low") double latLow,
            @Param("lng_low") double lngLow,
            @Param("lat_high") double latHigh,
            @Param("lng_high") double lngHigh,
            @Param("limit") int limit,
            @Param("offset") int offset);

    @Query(value = "select a.* from apartments a where a.published=true and upper(a.country_code)=upper(:countryCode) order by a.location <-> ST_GeomFromText( concat('SRID=4326;POINT(',:lng,' ',:lat,')') ) limit :limit offset :offset", nativeQuery = true)
    List<Apartment> findNearest(
            @Param("lng")double lng,
            @Param("lat") double lat,
            @Param("countryCode") String countryCode,
            @Param("limit") int limit,
            @Param("offset") int offset);

    // VK specific

    @Query("select a from VkontakteApartment a where a.externalId=:externalId order by created desc")
    List<VkontakteApartment> findVkAparmentsByExternalIdNewest(@Param("externalId") String externalId, Pageable pageable);

    @Query("select a from VkontakteApartment a where a.externalId in (:extenalIds) order by created desc")
    List<VkontakteApartment> findVkApartmentsByExternalIdIn(@Param("extenalIds")List<String> externalIds);

    @Query("select a from VkontakteApartment a")
    Page<VkontakteApartment> findVKAll(Pageable pageable);

    @Query("select count(a) from VkontakteApartment a")
    long countVK();

    @Query("select a from VkontakteApartment a where lower(a.description) like :text order by created desc")
    List<VkontakteApartment> findVkByQuery(@Param("text") String text, Pageable pageable);

    @Query("select count(a) from VkontakteApartment a where lower(a.description) like :text")
    long countVkByQuery(@Param("text") String text);

    // FB specific

    @Query("select a from FacebookApartment a where a.externalId=:externalId order by created desc")
    List<FacebookApartment> finFBAparmentsByExternalIdNewest(@Param("externalId") String externalId, Pageable pageable);

    @Query("select a from FacebookApartment a where a.externalId in (:extenalIds) order by created desc")
    List<FacebookApartment> findFBApartmentsByExternalIdIn(@Param("extenalIds")List<String> externalIds);

    @Query("select count(a) from FacebookApartment a")
    long countFB();

    @Query("select a from FacebookApartment a where lower(a.description) like :text order by created desc")
    Page<FacebookApartment> findFBAll(Pageable pageable);
}
