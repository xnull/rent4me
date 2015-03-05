package bynull.realty.converters.contacts;

import bynull.realty.common.Converter;
import bynull.realty.data.business.Contact;
import bynull.realty.dto.ContactDTO;

/**
 * Created by dionis on 3/5/15.
 */
public abstract class BaseContactModelDTOConverter<S extends Contact> implements ContactModelDTOConverter<S> {

    @Override
    public S newSourceType(ContactDTO in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ContactDTO toTargetType(S in, ContactDTO instance) {
        if(in == null) return null;
        instance.setId(in.getId());
        instance.setType(in.getType());

        return instance;
    }

    @Override
    public S toSourceType(ContactDTO in, S instance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ContactDTO newTargetType(S in) {
        return new ContactDTO();
    }
}
