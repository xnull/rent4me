package bynull.realty.converters.apartments;

import bynull.realty.converters.contacts.ContactModelDTOConverterFactory;
import bynull.realty.data.business.Contact;
import bynull.realty.data.business.SocialNetApartment;
import bynull.realty.dto.ApartmentDTO;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Created by dionis on 3/5/15.
 */
public abstract class SocialNetApartmentModelDTOConverter<T extends SocialNetApartment> extends BaseApartmentModelDTOConverter<T> {
    @Resource
    ContactModelDTOConverterFactory contactModelDTOConverterFactory;

    @Override
    public ApartmentDTO toTargetType(T apartment, ApartmentDTO dto) {
        if (apartment == null) {
            return null;
        }
        ApartmentDTO result = super.toTargetType(apartment, dto);

        Set<Contact> contacts = apartment.getContacts();

        return result;
    }
}
