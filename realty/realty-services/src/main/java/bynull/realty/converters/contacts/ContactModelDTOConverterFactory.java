package bynull.realty.converters.contacts;

import bynull.realty.common.ConverterFactory;
import bynull.realty.converters.apartments.ApartmentModelDTOConverter;
import bynull.realty.data.business.Contact;
import bynull.realty.data.business.PhoneContact;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ContactDTO;
import bynull.realty.utils.HibernateUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class ContactModelDTOConverterFactory<S extends Contact>  implements ConverterFactory<S, ContactDTO, ContactModelDTOConverter<S>> {

    private final ContactModelDTOConverter<S> NULL_CONVERTER = new ContactModelDTOConverter<S>() {
        @Override
        public ContactDTO newTargetType(S in) {
            return null;
        }

        @Override
        public S newSourceType(ContactDTO in) {
            return null;
        }

        @Override
        public ContactDTO toTargetType(S in, ContactDTO instance) {
            return null;
        }

        @Override
        public S toSourceType(ContactDTO in, S instance) {
            return null;
        }
    };
    @Resource
    PhoneContactModelDTOConverter phoneContactModelDTOConverter;

    @Override
    public ContactModelDTOConverter<S> getTargetConverter(S in) {
        in = HibernateUtil.deproxy(in);
        if(in instanceof PhoneContact) {
            return (ContactModelDTOConverter<S>) phoneContactModelDTOConverter;
        } else {
            if(in == null)
                return NULL_CONVERTER;
            else
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public ContactModelDTOConverter<S> getSourceConverter(ContactDTO in) {

        return null;
    }
}
