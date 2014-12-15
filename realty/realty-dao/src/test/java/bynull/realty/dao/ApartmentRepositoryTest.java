package bynull.realty.dao;

import bynull.realty.DbTest;
import bynull.realty.data.business.*;
import bynull.realty.data.common.GeoPoint;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ApartmentRepositoryTest extends DbTest {
    @Resource
    ApartmentRepository repository;

    @Test
    public void insert() {
        User user = createUser();
        Apartment apartment = new Apartment();
        apartment.setOwner(user);
        apartment.setTypeOfRent(RentType.LONG_TERM);
        apartment.setRentalFee(new BigDecimal("1000"));
        apartment.setFeePeriod(FeePeriod.MONTHLY);
        apartment.setFloorNumber(1);
        apartment.setFloorsTotal(2);
        apartment.setRoomCount(1);
        apartment.setLocation(new GeoPoint());
        apartment.setAddressComponents(new AddressComponents());
        apartment = repository.saveAndFlush(apartment);


        flushAndClear();

        Apartment found = repository.getOne(apartment.getId());
        assertThat(found, is(notNullValue()));
    }
}