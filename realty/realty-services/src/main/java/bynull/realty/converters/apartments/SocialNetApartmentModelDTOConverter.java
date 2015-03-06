package bynull.realty.converters.apartments;

import bynull.realty.converters.contacts.ContactModelDTOConverter;
import bynull.realty.converters.contacts.ContactModelDTOConverterFactory;
import bynull.realty.data.business.ApartmentExternalPhoto;
import bynull.realty.data.business.Contact;
import bynull.realty.data.business.SocialNetApartment;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ContactDTO;
import com.google.common.collect.Iterables;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/5/15.
 */
public abstract class SocialNetApartmentModelDTOConverter<T extends SocialNetApartment> extends BaseApartmentModelDTOConverter<T> {
    @Resource
    ContactModelDTOConverterFactory<Contact> contactModelDTOConverterFactory;

    @Override
    public ApartmentDTO toTargetType(T apartment, ApartmentDTO dto) {
        if (apartment == null) {
            return null;
        }
        ApartmentDTO result = super.toTargetType(apartment, dto);

        Set<Contact> contacts = apartment.getContacts();
        ContactModelDTOConverter<Contact> targetConverter = contactModelDTOConverterFactory.getTargetConverter(Iterables.getFirst(contacts, null));
        List<? extends ContactDTO> contactDTOs = targetConverter.toTargetList(contacts);
        result.setContacts(contactDTOs);

        result.setImageUrls(
                apartment.getExternalPhotos().stream()
                        .map(ApartmentExternalPhoto::getImageUrl)
                        .collect(Collectors.toList()));

        return result;
    }
}
