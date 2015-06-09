package bynull.realty.dao;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.FacebookApartment;
import bynull.realty.data.business.VkontakteApartment;
import com.google.common.collect.ImmutableList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author dionis on 22/06/14.
 */
public interface ApartmentRepository extends JpaRepository<Apartment, Long>, ApartmentRepositoryCustom {

    String LIST_APARTMENTS_QUERY_BASE = "select a.* from apartments a where a.data_source<>'INTERNAL' OR a.published=true ";
    String LIST_APARTMENTS_QUERY = LIST_APARTMENTS_QUERY_BASE + " order by a.id limit :limit offset :offset";

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


    @Query(value = "select a.* from apartments a where a.published=true order by a.location <-> ST_GeomFromText( concat('SRID=4326;POINT(',:lng,' ',:lat,')') ) limit :limit offset :offset", nativeQuery = true)
    List<Apartment> findNearest(
            @Param("lng")double lng,
            @Param("lat") double lat,
            @Param("limit") int limit,
            @Param("offset") int offset);



    // VK specific

    @Query("select a from VkontakteApartment a inner join a.vkontaktePage p where p.externalId=:externalId order by a.logicalCreated desc")
    List<VkontakteApartment> findVkAparmentsByExternalIdNewest(@Param("externalId") String externalId, Pageable pageable);

    @Query("select a from VkontakteApartment a where a.externalId in (:extenalIds) order by logicalCreated desc")
    List<VkontakteApartment> findVkApartmentsByExternalIdIn(@Param("extenalIds")List<String> externalIds);

    @Query("select a from VkontakteApartment a")
    Page<VkontakteApartment> findVKAll(Pageable pageable);

    @Query("select count(a) from VkontakteApartment a")
    long countVK();

    @Query("select a from VkontakteApartment a where lower(a.description) like :text order by logicalCreated desc")
    List<VkontakteApartment> findVkByQuery(@Param("text") String text, Pageable pageable);

    @Query("select count(a) from VkontakteApartment a where lower(a.description) like :text")
    long countVkByQuery(@Param("text") String text);

    // FB specific

    @Query("select a from FacebookApartment a inner join a.facebookPage p where p.externalId=:externalId order by a.logicalCreated desc")
    List<FacebookApartment> finFBAparmentsByExternalIdNewest(@Param("externalId") String externalId, Pageable pageable);

    @Query("select a from FacebookApartment a where a.externalId in (:extenalIds) order by logicalCreated desc")
    List<FacebookApartment> findFBApartmentsByExternalIdIn(@Param("extenalIds")List<String> externalIds);

    @Query("select count(a) from FacebookApartment a")
    long countFB();

    @Query("select a from FacebookApartment a")
    Page<FacebookApartment> findFBAll(Pageable pageable);

    @Query("select count(a) from Apartment a where lower(a.description)=lower(:text)")
    long countOfSimilarApartments(@Param("text") String text);

    @Modifying
    @Query("update Apartment a set a.published=false where a.logicalCreated < :date")
    void unPublishOldNonInternalApartments(@Param("date") Date date);

    @Query(value = "select count(tmp.*) from ("+LIST_APARTMENTS_QUERY_BASE+") tmp ", nativeQuery = true)
    long countOfActiveApartments();

    @Query(value = LIST_APARTMENTS_QUERY, nativeQuery = true)
    List<Apartment> listApartments(@Param("limit") int limit, @Param("offset") long offset);

    @Query(value = "select max(tmp.logical_created_dt) from ("+LIST_APARTMENTS_QUERY+") tmp ", nativeQuery = true)
    Date findMaxDateForPage(@Param("limit") int limit, @Param("offset") long offset);

    @Query("select a from FacebookApartment a where a.created >= :start_date and a.created < :end_date order by a.logicalCreated")
    List<FacebookApartment> findFBApartmentsSinceTime(@Param("start_date") Date date, @Param("end_date") Date endDate);

    List<Apartment> findByIdIn(List<Long> apartmentIds);


    public enum FindMode {
        RENTER {
            @Override
            public Apartment.Target toTarget() {
                return Apartment.Target.RENTER;
            }
        }, LESSOR {
            @Override
            public Apartment.Target toTarget() {
                return Apartment.Target.LESSOR;
            }
        };

        public abstract Apartment.Target toTarget();
    }

    /**
    * Created by dionis on 04/02/15.
    */
    enum RoomCount {
        ONE("1"), TWO("2"), THREE("3"), FOUR_PLUS("4+");

        public final String value;

        RoomCount(String value) {
            this.value = value;
        }

        public static RoomCount findByValueOrFail(String value) {
            if (value == null) {
                throw new IllegalArgumentException();
            }
            for (RoomCount roomCount : RoomCount.values()) {
                if (roomCount.value.equals(value)) return roomCount;
            }
            throw new IllegalArgumentException();
        }
    }
}
