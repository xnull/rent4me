package bynull.realty.services.impl.blacklist;

import bynull.realty.ServiceTest;
import bynull.realty.dao.ApartmentRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.services.api.ApartmentService;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by null on 8/2/15.
 */
public class BlacklistServiceImplTest extends ServiceTest {

    @Resource
    private BlacklistServiceImpl blService;

    @Resource
    private ApartmentRepository apartmentRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testAddApartmentToBlacklist() throws Exception {
        User user = createUser();

        InternalApartment apartment = new InternalApartment();
        apartment.setOwner(user);
        apartment.setTypeOfRent(RentType.LONG_TERM);
        apartment.setRentalFee(new BigDecimal("1000"));
        apartment.setFeePeriod(FeePeriod.MONTHLY);
        apartment.setFloorNumber(1);
        apartment.setFloorsTotal(2);
        apartment.setRoomCount(1);
        apartment.setLocation(new GeoPoint());
        apartment.setAddressComponents(new AddressComponents());
        apartment.setTarget(Apartment.Target.UNKNOWN);
        apartment.setDescription("description");

        apartment = apartmentRepository.saveAndFlush(apartment);

        flushAndClear();

        Apartment found = apartmentRepository.getOne(apartment.getId());

        blService.addApartmentToBlacklist(found.getId());


    }
}