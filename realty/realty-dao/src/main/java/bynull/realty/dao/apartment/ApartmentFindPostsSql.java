package bynull.realty.dao.apartment;

import bynull.realty.common.Porter;
import bynull.realty.dao.JooqUtil;
import bynull.realty.dao.apartment.ApartmentRepositoryCustom.FindPostsParameters;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.ids.IdentEntity;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.GeoPoint;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDSLContext;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by null on 10/18/15.
 */
public class ApartmentFindPostsSql {
    private static final Porter PORTER = Porter.getInstance();
    private final FindPostsParameters params;
    private final Optional<BoundingBox> boundingBox;
    private final String searchText;

    public ApartmentFindPostsSql(FindPostsParameters params, Optional<BoundingBox> boundingBox) {
        this.params = params;
        this.boundingBox = boundingBox;
        String text = StringUtils.trimToEmpty(params.text);
        searchText = text.isEmpty() ? "" : text.length() > 5 ? PORTER.stem(text) : text;
    }

    public String findPosts() {
        DSLContext ctx = new DefaultDSLContext(SQLDialect.POSTGRES_9_3);
        SelectJoinStep<?> query = ctx.select(DSL.field("a.*")).from(DSL.table("apartments").as("a"));
        query.where("a.id NOT IN (" + ApartmentRepositoryImpl.blackApartmentsQuery() + ")");
        query.where("a.published=true AND a.target IN (:targets)");

        if (!searchText.isEmpty()) {
            query.where("lower(a.description) like :msg");
        }
        if (!params.roomsCount.isEmpty()) {
            query.where("a.room_count IN (:roomCounts)");
        }
        if (params.minPrice != null) {
            query.where("a.rental_fee >= :minPrice");
        }
        if (params.maxPrice != null) {
            query.where("a.rental_fee <= :maxPrice");
        }
        if (!params.metroIds.isEmpty()) {
            query.where("a.id IN (select am.apartment_id from apartments_metros am where am.metro_station_id IN (:metroIds)) ");
        }
        if (isCountryPresent()) {
            query.where("upper(a.country_code)=upper(:country_code) ");
        }

        Optional<GeoPoint> point = params.geoParams.getPoint();
        Optional<BoundingBox> paramsBoundingBox = params.geoParams.getBoundingBox();
        if (point.isPresent() || paramsBoundingBox.isPresent()) {
            query.where("st_setsrid(st_makebox2d(ST_GeomFromText( concat('SRID=4326;POINT('," +
                    ":lng_low," +
                    "' '," +
                    ":lat_low," +
                    "')')), ST_GeomFromText( concat('SRID=4326;POINT('," +
                    ":lng_high," +
                    "' '," +
                    ":lat_high," +
                    "')'))), 4326)" +
                    " ~ a.location" +
                    " "
            );
        }

        if (point.isPresent()) {
            query.orderBy(DSL.field("a.location <-> ST_GeomFromText( concat('SRID=4326;POINT(',:lng,' ',:lat,')') ), a.logical_created_dt").desc());
        } else {
            query.orderBy(DSL.field("a.logical_created_dt").desc());
        }

        return query.toString();
    }

    public Map<String, Object> findPostsParamsMap() {
        Map<String, Object> paramsMap = new HashMap<>();
        if (!params.metroIds.isEmpty()) {
            paramsMap.put("metroIds", params.metroIds);
        }
        if (isCountryPresent()) {
            paramsMap.put("country_code", params.geoParams.getCountryCode().get());
        }

        Optional<GeoPoint> point = params.geoParams.getPoint();
        if (boundingBox.isPresent()) {
            BoundingBox box = boundingBox.get();
            paramsMap.put("lng_low", box.getLow().getLongitude());
            paramsMap.put("lat_low", box.getLow().getLatitude());
            paramsMap.put("lng_high", box.getHigh().getLongitude());
            paramsMap.put("lat_high", box.getHigh().getLatitude());
        } else if (point.isPresent()) {
            GeoPoint geoPoint = point.get();
            paramsMap.put("lng_low", geoPoint.getLongitude());
            paramsMap.put("lat_low", geoPoint.getLatitude());
            paramsMap.put("lng_high", geoPoint.getLongitude());
            paramsMap.put("lat_high", geoPoint.getLatitude());
        } else {
            throw new UnsupportedOperationException("Either boundning box or geo point should be present");
        }

        if (point.isPresent()) {
            paramsMap.put("lat", (point).get().getLatitude());
            paramsMap.put("lng", (point).get().getLongitude());
        }

        Apartment.Target value = params.findMode.toTarget();
        List<String> targets = ImmutableList.of(value, Apartment.Target.BOTH)
                .stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        paramsMap.put("targets", targets);

        if (!searchText.isEmpty()) {
            paramsMap.put("msg", ilike(searchText));
        }

        if (!params.roomsCount.isEmpty()) {
            Set<Integer> values = params.roomsCount.stream().map(rc -> Integer.valueOf(rc.value)).collect(Collectors.toSet());
            paramsMap.put("roomCounts", values);
        }

        if (params.minPrice != null) {
            paramsMap.put("minPrice", BigDecimal.valueOf(params.minPrice));
        }

        if (params.maxPrice != null) {
            paramsMap.put("maxPrice", BigDecimal.valueOf(params.maxPrice));
        }

        return paramsMap;
    }

    private boolean isCountryPresent() {
        return params.geoParams.getCountryCode().isPresent() && StringUtils.trimToNull(params.geoParams.getCountryCode().get()) != null;
    }

    private String ilike(String text) {
        return !text.isEmpty() ? "%" + text.toLowerCase() + "%" : "%";
    }
}
