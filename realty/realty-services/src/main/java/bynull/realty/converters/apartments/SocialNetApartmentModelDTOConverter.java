package bynull.realty.converters.apartments;

import bynull.realty.converters.contacts.ContactModelDTOConverter;
import bynull.realty.converters.contacts.ContactModelDTOConverterFactory;
import bynull.realty.data.business.Contact;
import bynull.realty.data.business.SocialNetApartment;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ApartmentExternalPhotoDTO;
import bynull.realty.dto.ContactDTO;
import com.google.common.collect.Iterables;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/5/15.
 */
public abstract class SocialNetApartmentModelDTOConverter<T extends SocialNetApartment> extends BaseApartmentModelDTOConverter<T> {
    @Resource
    ContactModelDTOConverterFactory<Contact> contactModelDTOConverterFactory;

    @Override
    public Optional<ApartmentDTO> toTargetType(Optional<T> apartment, ApartmentDTO dto) {
        return apartment.flatMap(ap -> {
            ApartmentDTO result = super.toTargetType(apartment, dto).get();//not null guaranties

            result.setExternalLink(ap.getLink());
            result.setExternalAuthorLink(ap.getExtAuthorLink());

            Set<Contact> contacts = ap.getContacts();
            ContactModelDTOConverter<Contact> targetConverter = contactModelDTOConverterFactory.getTargetConverter(Iterables.getFirst(contacts, null));
            List<? extends ContactDTO> contactDTOs = targetConverter.toTargetList(contacts);
            result.setContacts(contactDTOs);

            result.setImageUrls(
                    ap.getExternalPhotos().stream()
                            .map(ApartmentExternalPhotoDTO::from)
                            .collect(Collectors.toList()));

            return Optional.of(result);
        });
    }
}
