package bynull.realty.dao;

import bynull.realty.DbTest;
import bynull.realty.dao.apartment.ApartmentRepository;
import bynull.realty.dao.external.FacebookPageToScrapRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.data.common.GeoPoint;
import com.google.common.collect.Iterables;
import org.junit.Test;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

public class ApartmentRepositoryTest extends DbTest {
    @Resource
    ApartmentRepository repository;

    @Resource
    FacebookPageToScrapRepository facebookPageToScrapRepository;

    @Test
    public void insert() {
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
        apartment = repository.saveAndFlush(apartment);


        flushAndClear();

        Apartment found = repository.getOne(apartment.getId());
        assertThat(found, is(notNullValue()));
    }

    @Test
    public void insertInternalApartmentWithWrongFloorNumberCountShouldFail() {
        User user = createUser();
        InternalApartment apartment = new InternalApartment();
        apartment.setOwner(user);
        apartment.setTypeOfRent(RentType.LONG_TERM);
        apartment.setRentalFee(new BigDecimal("1000"));
        apartment.setFeePeriod(FeePeriod.MONTHLY);
        apartment.setFloorNumber(0);
        apartment.setFloorsTotal(2);
        apartment.setRoomCount(1);
        apartment.setLocation(new GeoPoint());
        apartment.setAddressComponents(new AddressComponents());
        apartment.setTarget(Apartment.Target.UNKNOWN);
        try {
            apartment = repository.saveAndFlush(apartment);
            fail("Exception expected");
        } catch (ConstraintViolationException expected) {
            Set<ConstraintViolation<?>> constraintViolations = expected.getConstraintViolations();
            assertThat(constraintViolations.size(), is(1));
            ConstraintViolation<?> first = Iterables.getFirst(constraintViolations, null);
            assertThat(first.getMessage(), is(notNullValue()));
            assertThat(first.getMessage(), is("must be greater than or equal to 1"));
            assertThat(String.valueOf(first.getPropertyPath()), is("floorNumber"));
        }
    }

    @Test
    public void insertFacebookAnyFloorNumberCountShouldBeOk() {
        FacebookPageToScrap fbPage = new FacebookPageToScrap();
        fbPage.setExternalId("abc");

        fbPage = facebookPageToScrapRepository.saveAndFlush(fbPage);

        FacebookApartment apartment = new FacebookApartment();
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

        Apartment found = repository.getOne(apartment.getId());
        assertThat(found, is(notNullValue()));
    }

    @Test
    public void findNearestPoints() {
        final String majakaDescription = "Majaka";
        {
            InternalApartment apartment = new InternalApartment();
            apartment.setDescription(majakaDescription);
            apartment.setOwner(createUser());
            apartment.setTypeOfRent(RentType.LONG_TERM);
            apartment.setRentalFee(new BigDecimal("1000"));
            apartment.setFeePeriod(FeePeriod.MONTHLY);
            apartment.setFloorNumber(1);
            apartment.setFloorsTotal(2);
            apartment.setRoomCount(1);

            GeoPoint location = new GeoPoint();
            location.setLongitude(24.7931234);
            location.setLatitude(59.4282397);
            apartment.setLocation(location);
            apartment.setPublished(true);
            AddressComponents addressComponents = new AddressComponents();
            addressComponents.setCountryCode("EE");
            apartment.setAddressComponents(addressComponents);
            apartment.setTarget(Apartment.Target.UNKNOWN);
            apartment = repository.saveAndFlush(apartment);

        }

        final String sytiste33Description = "Sütiste tee 33";
        {
            InternalApartment apartment = new InternalApartment();
            apartment.setDescription(sytiste33Description);
            apartment.setOwner(createUser());
            apartment.setTypeOfRent(RentType.LONG_TERM);
            apartment.setRentalFee(new BigDecimal("1000"));
            apartment.setFeePeriod(FeePeriod.MONTHLY);
            AddressComponents addressComponents = new AddressComponents();
            addressComponents.setCountryCode("EE");
            apartment.setAddressComponents(addressComponents);
            apartment.setFloorNumber(1);
            apartment.setFloorsTotal(2);
            apartment.setRoomCount(1);
            apartment.setPublished(true);

            GeoPoint location = new GeoPoint();
            /**
             * POINT(24.6807756000001 59.3972856)
             */
            location.setLongitude(24.6807756000001);
            location.setLatitude(59.3972856);
            apartment.setTarget(Apartment.Target.UNKNOWN);
            apartment.setLocation(location);
            apartment = repository.saveAndFlush(apartment);
        }

        assertThat(repository.count(), is(2L));

        List<Apartment> all = repository.findAll();

        assertThat(all.size(), is(2));

        for (Apartment apartment : all) {
            assertThat(apartment.getAddressComponents().getCountryCode(), is("EE"));
        }

        {
            //sütiste tee 33 should be first
            GeoPoint location = new GeoPoint();
            location.setLongitude(24.6811760313352);
            location.setLatitude(59.3965599419749);
            List<Apartment> nearest = repository.findNearest(location.getLongitude(), location.getLatitude(), "EE", 100, 0);
            assertThat(nearest.size(), is(2));

            Apartment first = Iterables.getFirst(nearest, null);
            Apartment second = Iterables.get(nearest, 1, null);

            assertThat(first, is(not(second)));
            assertThat(first.getDescription(), is(sytiste33Description));
            assertThat(second.getDescription(), is(majakaDescription));
        }
    }

    @Test
    public void unspublish() throws Exception {
        repository.unPublishOldNonInternalApartments(new Date(System.currentTimeMillis() - (31*24*60*60*1000)));
    }
}