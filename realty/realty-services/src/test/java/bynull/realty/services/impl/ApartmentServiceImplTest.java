package bynull.realty.services.impl;

import bynull.realty.ServiceTest;
import bynull.realty.dao.apartment.ApartmentIdentRepository;
import bynull.realty.dao.apartment.ApartmentRepository;
import bynull.realty.dao.api.ident.IdentRepository;
import bynull.realty.dao.external.FacebookPageToScrapRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.data.common.GeoPoint;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Created by null on 9/13/15.
 */
public class ApartmentServiceImplTest extends ServiceTest {

    @Resource
    private ApartmentServiceImpl aptService;
    @Resource
    private ApartmentRepository repository;
    @Resource
    FacebookPageToScrapRepository facebookPageToScrapRepository;
    @Resource
    private IdentRepository identRepo;
    @Resource
    private ApartmentIdentRepository apartmentIdentRepo;

    @Test
    public void testSaveIdents() throws Exception {
        Long aptId = createTestFbApt();
        aptService.saveIdents(aptId);
        assertEquals(2, identRepo.findAll().size());
        assertEquals(1, apartmentIdentRepo.findAll().size());
    }

    private Long createTestFbApt() {
        FacebookPageToScrap fbPage = new FacebookPageToScrap();
        fbPage.setExternalId("abc");

        fbPage = facebookPageToScrapRepository.saveAndFlush(fbPage);

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
        apartment = repository.saveAndFlush(apartment);

        flushAndClear();
        return apartment.getId();
    }
}