package bynull.realty.dao;

import bynull.realty.DbTest;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.FeePeriod;
import bynull.realty.data.business.RentType;
import bynull.realty.data.business.User;
import junit.framework.TestCase;
import org.junit.Test;

import javax.annotation.Resource;
import java.math.BigDecimal;

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
        apartment = repository.saveAndFlush(apartment);

    }
}