package bynull.realty.dao.apartment;

import bynull.realty.dao.apartment.ApartmentRepository.FindMode;
import bynull.realty.dao.apartment.ApartmentRepository.RoomCount;
import bynull.realty.dao.apartment.ApartmentRepositoryCustom.FindPostsParameters;
import bynull.realty.dao.apartment.ApartmentRepositoryCustom.GeoParams;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.util.LimitAndOffset;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by null on 10/18/15.
 */
public class ApartmentFindPostsSqlTest {

    @Test
    public void testFindPosts() throws Exception {
        HashSet<RoomCount> roomCount = new HashSet<>();
        roomCount.add(RoomCount.TWO);
        BoundingBox boundingBox = new BoundingBox();
        LimitAndOffset offset = LimitAndOffset.builder().withOffset(1).withLimit(1).create();
        GeoParams geo = new GeoParams();
        geo = geo.withCountryCode(Optional.of("RU"));
        geo = geo.withBoundingBox(Optional.of(boundingBox));
        geo = geo.withPoint(Optional.of(new GeoPoint(55, 55)));

        FindPostsParameters params = new FindPostsParameters(
                "you", true, roomCount, 1, 100, FindMode.RENTER, geo, new ArrayList<>(), offset
        );
        ApartmentFindPostsSql aptFindPost = new ApartmentFindPostsSql(params, Optional.of(boundingBox));
        assertEquals(
                "select a.* from apartments as \"a\" where (   (a.id not in (select aptident.apartment_id from ident as \"ident\"   join blacklist black   on (ident.id = black.ident_id)   join apartment_ident aptident   on (ident.id = aptident.ident_id)))   and (a.published=true and a.target in (:targets))   and (lower(a.description) like :msg)   and (a.room_count in (:roomcounts))   and (a.rental_fee >= :minprice)   and (a.rental_fee <= :maxprice)   and (upper(a.country_code)=upper(:country_code) )   and (st_setsrid(st_makebox2d(st_geomfromtext( concat('srid=4326;point(',:lng_low,' ',:lat_low,')')), st_geomfromtext( concat('srid=4326;point(',:lng_high,' ',:lat_high,')'))), 4326) ~ a.location ) ) order by a.location <-> st_geomfromtext( concat('srid=4326;point(',:lng,' ',:lat,')') ), a.logical_created_dt desc",
                aptFindPost.findPosts().toLowerCase().replaceAll("\n", " ")
        );
    }
}