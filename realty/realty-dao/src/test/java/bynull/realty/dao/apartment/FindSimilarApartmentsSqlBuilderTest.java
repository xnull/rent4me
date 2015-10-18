package bynull.realty.dao.apartment;

import bynull.realty.data.business.*;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.data.common.BoundingBox;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.GeoPoint;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by null on 10/18/15.
 */
public class FindSimilarApartmentsSqlBuilderTest {

    @Test
    public void testFindSimilarApartmentsQuery() throws Exception {
        FindSimilarApartmentsSqlBuilder sqlBuilder = new FindSimilarApartmentsSqlBuilder(testApartment(), 123L);

        assertEquals(
                "select a.* from apartments as \"a\" where (   (a.id not in (select aptident.apartment_id from ident as \"ident\"   join blacklist black   on (ident.id = black.ident_id)   join apartment_ident aptident   on (ident.id = aptident.ident_id)))   and (a.id<>:id)   and (a.room_count=:room_count)   and (a.rental_fee >= :min_fee and a.rental_fee <= :max_fee)   and (st_setsrid(st_makebox2d(st_geomfromtext( concat('srid=4326;point(',:lng_low,' ',:lat_low,')')), st_geomfromtext( concat('srid=4326;point(',:lng_high,' ',:lat_high,')'))), 4326) ~ a.location) ) order by a.logical_created_dt desc",
                sqlBuilder.findSimilarApartmentsQuery().replaceAll("\n", " ").toLowerCase()
        );
    }

    private FacebookApartment testApartment() {
        CityEntity city = new CityEntity();
        city.setName("Москва");
        city.setArea(new BoundingBox(new GeoPoint(55, 55), new GeoPoint(55, 55)));

        FacebookPageToScrap fbPage = new FacebookPageToScrap();
        fbPage.setExternalId("abc");
        fbPage.setCity(city);

        FacebookApartment apartment = new FacebookApartment();
        apartment.setExtAuthorLink("https://www.facebook.com/131345607204302");
        apartment.setTypeOfRent(RentType.LONG_TERM);
        apartment.setRentalFee(new BigDecimal("1000"));
        apartment.setFeePeriod(FeePeriod.MONTHLY);
        apartment.setFloorNumber(0);
        apartment.setFloorsTotal(2);
        apartment.setRoomCount(1);
        apartment.setLocation(new GeoPoint());
        apartment.setAddressComponents(new AddressComponents());
        apartment.setFacebookPage(fbPage);
        apartment.setExternalId("asd");
        apartment.setTarget(Apartment.Target.UNKNOWN);


        return apartment;
    }
}