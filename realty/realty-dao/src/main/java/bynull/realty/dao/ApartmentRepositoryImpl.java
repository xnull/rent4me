package bynull.realty.dao;

import bynull.realty.common.Porter;
import bynull.realty.dao.geo.CityRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.utils.HibernateUtil;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/5/15.
 */
public class ApartmentRepositoryImpl implements ApartmentRepositoryCustom, InitializingBean {
    @PersistenceContext
    EntityManager entityManager;

    @Resource
    CityRepository cityRepository;

    Porter porter;

    @Override
    public void afterPropertiesSet() throws Exception {
        porter = Porter.getInstance();
    }

    @Override
    public List<Apartment> findSimilarApartments(long apartmentId) {
        Apartment apartment = entityManager.find(Apartment.class, apartmentId);
        if (apartment == null) {
            throw new EntityNotFoundException("Apartment with id " + apartmentId + " not found");
        }
        apartment = HibernateUtil.deproxy(apartment);

        String sql = "SELECT a.* FROM apartments a WHERE a.id<>:id ";
        Map<String, Object> params = new HashMap<>();
        params.put("id", apartmentId);

        String countryCode = apartment.getAddressComponents().getCountryCode();
        if (countryCode != null) {
            sql += " AND  a.country_code=:country_code ";
            params.put("country_code", countryCode);
        }

        if(apartment.getRoomCount() != null) {
            sql += " AND a.room_count=:room_count ";
            params.put("room_count", apartment.getRoomCount());
        }

        if (apartment.getRentalFee() != null) {
            sql += " AND a.rental_fee >= :min_fee AND a.rental_fee <= :max_fee ";
            int rentalFee = apartment.getRentalFee().intValue();

            final int base = 10000;
            int minBase = rentalFee / base;
            int maxBase = minBase + 1;

            int minFee = minBase * base;
            int maxFee = maxBase * base;

            params.put("min_fee", minFee);
            params.put("max_fee", maxFee);
        }

        Set<MetroEntity> metros = apartment.getMetros();
        if(metros != null && !metros.isEmpty()) {
            sql += " AND a.id IN (select apartment_id from apartments_metros where metro_station_id IN (:target_metro_ids))";
            Set<Long> targetMetroIds = metros.stream().map(MetroEntity::getId).collect(Collectors.toSet());
            params.put("target_metro_ids", targetMetroIds);
        }

        String defaultSorting = "a.logical_created_dt desc";
        final String targetSorting;
        //filtering
        if(apartment instanceof SocialNetApartment) {

            SocialNetApartment socialNetApartment = (SocialNetApartment) apartment;

            CityEntity city = socialNetApartment.getCity();
            if (city != null) {
                String boundingBoxSQL = " AND st_setsrid(st_makebox2d(ST_GeomFromText( concat('SRID=4326;POINT('," +
                        ":lng_low," +
                        "' '," +
                        ":lat_low," +
                        "')')), ST_GeomFromText( concat('SRID=4326;POINT('," +
                        ":lng_high," +
                        "' '," +
                        ":lat_high," +
                        "')'))), 4326)" +
                        " ~ a.location";

                sql += boundingBoxSQL;

                params.put("lng_low", city.getArea().getLow().getLongitude());
                params.put("lat_low", city.getArea().getLow().getLatitude());
                params.put("lng_high", city.getArea().getHigh().getLongitude());
                params.put("lat_high", city.getArea().getHigh().getLatitude());
            }
        }

        //sorting
        if (apartment instanceof SocialNetApartment) {
            targetSorting = defaultSorting;
        } else if (apartment instanceof InternalApartment) {

            targetSorting = "a.location <-> ST_GeomFromText( concat('SRID=4326;POINT(',:lng,' ',:lat,')') ),"+defaultSorting;
            params.put("lng", apartment.getLocation().getLongitude());
            params.put("lat", apartment.getLocation().getLatitude());
        } else {
            throw new UnsupportedOperationException("Unsupported sorting type for class"+apartment.getClass());
        }

        sql += " ORDER BY "+targetSorting;

        Query query = entityManager.createNativeQuery(sql, Apartment.class)
                .setMaxResults(10);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        @SuppressWarnings("unchecked")
        List<Apartment> resultList = query.getResultList();

        return resultList;
    }

    @Override
    public List<Apartment> findPosts(String text,
                                     boolean withSubway,
                                     Set<ApartmentRepository.RoomCount> roomsCount,
                                     Integer minPrice,
                                     Integer maxPrice,
                                     ApartmentRepository.FindMode findMode,
                                     GeoParams geoParams,
                                     List<Long> metroIds, LimitAndOffset limitAndOffset) {
        Assert.notNull(text);
        Assert.notNull(roomsCount);
        Assert.notNull(findMode);
        Assert.notNull(geoParams);

        Assert.notNull(roomsCount);
        text = StringUtils.trimToEmpty(text);

        String searchText = text.isEmpty() ? "" : text.length() > 5 ? porter.stem(text) : text;

        String qlString = "select a.* from apartments a where a.published=true AND a.target IN (:targets) " +
                (!searchText.isEmpty() ? " AND lower(a.description) like :msg " : "") +
//                (withSubway ? " AND p.metros IS NOT EMPTY " : "") +
                (!roomsCount.isEmpty() ? " AND a.room_count IN (:roomCounts) " : "") +
                (minPrice != null ? " AND a.rental_fee >= :minPrice " : "") +
                (maxPrice != null ? " AND a.rental_fee <= :maxPrice " : "")
                ;

        Map<String, Object> params = new HashMap<>();

        if(!metroIds.isEmpty()) {
            qlString += " AND a.id IN (select am.apartment_id from apartments_metros am where am.metro_station_id IN (:metroIds)) ";

            params.put("metroIds", metroIds);
        }

        final String ordering;

        if(geoParams.getCountryCode().isPresent() && StringUtils.trimToNull(geoParams.getCountryCode().get()) != null) {
            qlString += " AND upper(a.country_code)=upper(:country_code) ";
            params.put("country_code", geoParams.getCountryCode().get());
        }


        Optional<GeoPoint> point = geoParams.getPoint();
        Optional<BoundingBox> paramsBoundingBox = geoParams.getBoundingBox();
        if(point.isPresent() || paramsBoundingBox.isPresent()) {
            final Optional<BoundingBox> boundingBox;
            if (paramsBoundingBox.isPresent()) {
                boundingBox = paramsBoundingBox;
            } else {
                CityEntity city = cityRepository.findByPoint(point.get().getLongitude(), point.get().getLatitude());
                boundingBox = Optional.<BoundingBox>ofNullable(city != null ? city.getArea() : null);
            }

            qlString += " AND st_setsrid(st_makebox2d(ST_GeomFromText( concat('SRID=4326;POINT('," +
                    ":lng_low," +
                    "' '," +
                    ":lat_low," +
                    "')')), ST_GeomFromText( concat('SRID=4326;POINT('," +
                    ":lng_high," +
                    "' '," +
                    ":lat_high," +
                    "')'))), 4326)" +
                    " ~ a.location" +
                    " ";

            if(boundingBox.isPresent()) {
                BoundingBox box = boundingBox.get();
                params.put("lng_low", box.getLow().getLongitude());
                params.put("lat_low", box.getLow().getLatitude());
                params.put("lng_high", box.getHigh().getLongitude());
                params.put("lat_high", box.getHigh().getLatitude());
            } else if(point.isPresent()) {
                GeoPoint geoPoint = point.get();
                params.put("lng_low", geoPoint.getLongitude());
                params.put("lat_low", geoPoint.getLatitude());
                params.put("lng_high", geoPoint.getLongitude());
                params.put("lat_high", geoPoint.getLatitude());
            } else {
                throw new UnsupportedOperationException("Either boundning box or geo point should be present");
            }
        }

        if(point.isPresent()) {
            ordering = " order by a.location <-> ST_GeomFromText( concat('SRID=4326;POINT(',:lng,' ',:lat,')') ), a.logical_created_dt DESC";
            params.put("lat", (point).get().getLatitude());
            params.put("lng", (point).get().getLongitude());
        } else {
            ordering = " ORDER BY a.logical_created_dt DESC";
        }


        qlString += ordering;

        Query query = entityManager.createNativeQuery(qlString, Apartment.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        Apartment.Target value = findMode.toTarget();
        query.setParameter("targets", ImmutableList.of(value, Apartment.Target.BOTH).stream().map(Enum::name).collect(Collectors.toList()));

        if (!searchText.isEmpty()) {
            query.setParameter("msg", ilike(searchText));
        }

        if (!roomsCount.isEmpty()) {
            Set<Integer> values = roomsCount.stream().map(rc -> Integer.valueOf(rc.value)).collect(Collectors.toSet());
            query.setParameter("roomCounts", values);
        }

        if (minPrice != null) {
            query.setParameter("minPrice", BigDecimal.valueOf(minPrice));
        }

        if (maxPrice != null) {
            query.setParameter("maxPrice", BigDecimal.valueOf(maxPrice));
        }

        @SuppressWarnings("unchecked")
        List<Apartment> resultList = query
                .setFirstResult(limitAndOffset.offset)
                .setMaxResults(limitAndOffset.limit)
                .getResultList();

        return resultList;
    }

    private String ilike(String text) {
        return !text.isEmpty() ? "%" + text.toLowerCase() + "%" : "%";
    }

    @Override
    public Set<String> similarApartments(Set<String> hashes) {
        Assert.notNull(hashes);
        if(hashes.isEmpty()) return Collections.emptySet();

        Query query = entityManager.createNativeQuery("select a.description from apartments a where a.description_hash IN (:hashez)");
        query.setParameter("hashez", hashes);

        Set<String> result = (Set<String>) query.getResultList().stream().collect(Collectors.toSet());
        return result;
    }
}
