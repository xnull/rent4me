package bynull.realty.services.impl.blacklist;

import bynull.realty.ServiceTest;
import bynull.realty.dao.ApartmentRepository;
import bynull.realty.dao.api.ident.IdRelationsRepository;
import bynull.realty.dao.external.VkontaktePageRepository;
import bynull.realty.data.business.*;
import bynull.realty.data.business.blacklist.BlacklistEntity;
import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import bynull.realty.data.business.ids.IdRelationEntity;
import bynull.realty.data.business.ids.IdentEntity;
import bynull.realty.data.business.ids.IdentType;
import bynull.realty.data.common.GeoPoint;
import bynull.realty.services.impl.IdentificationServiceImpl;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Resource
    private IdentificationServiceImpl identService;

    @Resource
    private IdRelationsRepository relationsRepo;

    @Test
    @Transactional
    @Rollback(false)
    public void testAddApartmentToBlacklist() throws Exception {
        User user = createUser();
        InternalApartment apartment = createInternalApartment(user);
        VkontakteApartment vkApt = createVkapartment();
        VkontakteApartment vkAptAnother = createVkapartmentAnother();

        flushAndClear();

        Apartment foundInternal = apartmentRepository.getOne(apartment.getId());
        Apartment foundVk = apartmentRepository.getOne(vkApt.getId());
        Apartment foundVkAnother = apartmentRepository.getOne(vkAptAnother.getId());

        blService.addApartmentToBlacklist(foundInternal.getId());
        blService.addApartmentToBlacklist(foundVk.getId());
        blService.addApartmentToBlacklist(foundVkAnother.getId());

        Set<IdentEntity> aptIdents = identService.findAllLinkedIdents(apartment.getId().toString(), IdentType.APARTMENT);
        assertEquals(8, aptIdents.size());
        assertTrue(aptIdents.stream().map(IdentEntity::getValue).collect(Collectors.toSet()).contains("8-915-471-28-71"));

        IdentEntity phone = aptIdents.stream().filter(id -> id.getType().equals(IdentType.PHONE.getType())).findFirst().get();
        assertEquals(0, relationsRepo.findBySourceId(phone.getId()).size());
        assertEquals(2, relationsRepo.findByAdjacentId(phone.getId()).size());

        Optional<BlacklistEntity> bl = blService.find(apartment.getId().toString(), IdentType.APARTMENT);
        assertNotEquals(Optional.empty(), bl);

        Set<IdentEntity> vkIdents = identService.findAllLinkedIdents("8-915-471-28-71", IdentType.PHONE);
        assertEquals(8, vkIdents.size());
        assertTrue(vkIdents.stream().map(IdentEntity::getValue).collect(Collectors.toSet()).contains("8-915-471-28-71"));

        Optional<BlacklistEntity> phoneBl = blService.find("8-915-471-28-71", IdentType.PHONE);
        assertNotEquals(Optional.empty(), phoneBl);

        Optional<BlacklistEntity> vkBl = blService.find("id248324164", IdentType.VK_ID);
        assertNotEquals(Optional.empty(), vkBl);

        Set<IdentEntity> anotherAptIdents = identService.findAllLinkedIdents(foundVkAnother.getId().toString(), IdentType.APARTMENT);
        IdentEntity aloneVk = anotherAptIdents.stream().filter(id -> id.getType().equals(IdentType.APARTMENT.getType())).findFirst().get();
        assertEquals(3, anotherAptIdents.size());
        assertEquals(2, relationsRepo.findBySourceId(aloneVk.getId()).size());
        assertEquals(0, relationsRepo.findByAdjacentId(aloneVk.getId()).size());
    }

    private VkontakteApartment createVkapartment() {
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

    private VkontakteApartment createVkapartmentAnother() {
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
        vkPage.setExternalId("123321");
        vkPageRepo.save(vkPage);

        apartment.setVkontaktePage(vkPage);
        apartment.setExternalId("11223343334");
        apartment.setExtAuthorLink("https://vk.com/id555");

        Set<Contact> contacts = new HashSet<>();
        PhoneContact phone = new PhoneContact();
        PhoneNumber number = new PhoneNumber();
        number.setRawNumber("8-915-471-28-72");
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