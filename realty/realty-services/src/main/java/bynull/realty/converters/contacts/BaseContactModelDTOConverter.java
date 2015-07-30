package bynull.realty.converters.contacts;

import bynull.realty.data.business.Contact;
import bynull.realty.dto.ContactDTO;

import java.util.Optional;

/**
 * Created by dionis on 3/5/15.
 */
public abstract class BaseContactModelDTOConverter<S extends Contact> implements ContactModelDTOConverter<S> {

    @Override
    public S newSourceType(ContactDTO in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<ContactDTO> toTargetType(Optional<S> in, ContactDTO instance) {
        return in.flatMap(s -> {
            instance.setId(s.getId());
            instance.setType(s.getType());

            return Optional.of(instance);
        });
    }

    @Override
    public S toSourceType(ContactDTO in, S instance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ContactDTO newTargetType(Optional<S> in) {
        return new ContactDTO();
    }
}
