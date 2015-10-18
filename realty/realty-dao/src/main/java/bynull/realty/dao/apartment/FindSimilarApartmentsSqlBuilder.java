package bynull.realty.dao.apartment;

import bynull.realty.dao.JooqUtil;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.InternalApartment;
import bynull.realty.data.business.SocialNetApartment;
import bynull.realty.data.business.ids.IdentEntity;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.CityEntity;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDSLContext;

import javax.persistence.criteria.CriteriaQuery;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by null on 10/18/15.
 */
class FindSimilarApartmentsSqlBuilder {
    private final Apartment apartment;
    private final String countryCode;
    private final Set<MetroEntity> metros;
    private final boolean isSocialNetApartment;
    private final boolean isInternalApartment;
    private final CityEntity city;
    private final Long apartmentId;

    public FindSimilarApartmentsSqlBuilder(Apartment apartment, Long apartmentId) {
        this.apartment = apartment;
        this.apartmentId = apartmentId;
        countryCode = apartment.getAddressComponents().getCountryCode();
        metros = apartment.getMetros();
        isSocialNetApartment = apartment instanceof SocialNetApartment;
        isInternalApartment = apartment instanceof InternalApartment;
        city = getCity();
    }

    String findSimilarApartmentsQuery() {
        DSLContext ctx = new DefaultDSLContext(SQLDialect.POSTGRES_9_3);
        SelectJoinStep<?> query = ctx.select(DSL.field("a.*")).from(DSL.table("apartments").as("a"));
        query.where("a.id NOT IN (" + ApartmentRepositoryImpl.blackApartmentsQuery() + ")");
        query.where("a.id<>:id");
        if (countryCode != null) {
            query.where("a.country_code=:country_code");
        }
        if (apartment.getRoomCount() != null) {
            query.where("a.room_count=:room_count");
        }

        if (apartment.getRentalFee() != null) {
            query.where("a.rental_fee >= :min_fee AND a.rental_fee <= :max_fee");
        }

        if (!metros.isEmpty()) {
            query.where("a.id IN (select apartment_id from apartments_metros where metro_station_id IN (:target_metro_ids))");
        }

        //filtering
        if (city != null) {
            String boundingBoxSQL = "st_setsrid(st_makebox2d(ST_GeomFromText( concat('SRID=4326;POINT('," +
                    ":lng_low," +
                    "' '," +
                    ":lat_low," +
                    "')')), ST_GeomFromText( concat('SRID=4326;POINT('," +
                    ":lng_high," +
                    "' '," +
                    ":lat_high," +
                    "')'))), 4326)" +
                    " ~ a.location";

            query.where(boundingBoxSQL);
        }

        //sorting
        final String targetSorting;
        String defaultSorting = "a.logical_created_dt";
        if (isSocialNetApartment) {
            targetSorting = defaultSorting;
        } else if (isInternalApartment) {
            targetSorting = "a.location <-> ST_GeomFromText( concat('SRID=4326;POINT(',:lng,' ',:lat,')') )," + defaultSorting;
        } else {
            throw new UnsupportedOperationException("Unsupported sorting type for class" + apartment.getClass());
        }
        query.orderBy(DSL.field(targetSorting).desc());

        return query.toString();
    }

    Map<String, Object> findSimilarApartmentsMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", apartmentId);
        if (countryCode != null) {
            params.put("country_code", countryCode);
        }

        if (apartment.getRoomCount() != null) {
            params.put("room_count", apartment.getRoomCount());
        }

        if (apartment.getRentalFee() != null) {
            int rentalFee = apartment.getRentalFee().intValue();

            final int base = 10000;
            int minBase = rentalFee / base;
            int maxBase = minBase + 1;

            int minFee = minBase * base;
            int maxFee = maxBase * base;

            params.put("min_fee", minFee);
            params.put("max_fee", maxFee);
        }

        if (!metros.isEmpty()) {
            Set<Long> targetMetroIds = metros.stream().map(MetroEntity::getId).collect(Collectors.toSet());
            params.put("target_metro_ids", targetMetroIds);
        }

        if (city != null) {
            params.put("lng_low", city.getArea().getLow().getLongitude());
            params.put("lat_low", city.getArea().getLow().getLatitude());
            params.put("lng_high", city.getArea().getHigh().getLongitude());
            params.put("lat_high", city.getArea().getHigh().getLatitude());
        }

        if (isInternalApartment) {
            params.put("lng", apartment.getLocation().getLongitude());
            params.put("lat", apartment.getLocation().getLatitude());
        } else if (!isSocialNetApartment) {
            throw new UnsupportedOperationException("Unsupported sorting type for class" + apartment.getClass());
        }

        return params;
    }

    private CityEntity getCity() {
        if (!isSocialNetApartment) {
            return null;
        }

        SocialNetApartment socialNetApartment = (SocialNetApartment) apartment;
        return socialNetApartment.getCity();
    }
}
