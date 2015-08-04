package bynull.realty.services.impl.blacklist;

import bynull.realty.ServiceTest;
import bynull.realty.dao.ApartmentRepository;
import bynull.realty.dao.external.VkontaktePageRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.business.blacklist.BlacklistEntity;
import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import bynull.realty.data.business.ids.IdentType;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.services.api.ApartmentService;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by null on 8/2/15.
 */
public class BlacklistServiceImplTest extends ServiceTest {

    @Resource
    private BlacklistServiceImpl blService;

    @Resource
    private ApartmentRepository apartmentRepository;

    @Resource
    private VkontaktePageRepository vkPageRepo;

    @Test
    @Transactional
    @Rollback(false)
    public void testAddApartmentToBlacklist() throws Exception {
        User user = createUser();
        InternalApartment apartment = createInternalApartment(user);
        VkontakteApartment vkApt = createFbapartment();

        flushAndClear();

        Apartment foundInternal = apartmentRepository.getOne(apartment.getId());
        Apartment foundVk = apartmentRepository.getOne(vkApt.getId());
        blService.addApartmentToBlacklist(foundInternal.getId());
        blService.addApartmentToBlacklist(foundVk.getId());

        Optional<BlacklistEntity> bl = blService.find(apartment.getId().toString(), IdentType.APARTMENT);
        assertNotEquals(Optional.empty(), bl);

        Optional<BlacklistEntity> vkBl = blService.find(vkApt.getId().toString(), IdentType.APARTMENT);
        assertNotEquals(Optional.empty(), vkBl);
    }

    private VkontakteApartment createFbapartment() {
        VkontakteApartment apartment = new VkontakteApartment();
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

        VkontaktePage vkPage = new VkontaktePage();
        vkPage.setExternalId("123");
        vkPageRepo.save(vkPage);

        apartment.setVkontaktePage(vkPage);
        apartment.setExternalId("11223344");
        apartment.setExtAuthorLink("https://vk.com/id248324164");

        Set<Contact> contacts = new HashSet<>();
        PhoneContact phone = new PhoneContact();
        PhoneNumber number = new PhoneNumber();
        number.setRawNumber("8-915-471-28-71");
        phone.setPhoneNumber(number);
        contacts.add(phone);
        apartment.setContacts(contacts);

        apartment = apartmentRepository.saveAndFlush(apartment);
        return apartment;
    }

    private InternalApartment createInternalApartment(User user) {
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
        return apartment;
    }
}