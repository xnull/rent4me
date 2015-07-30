package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.ContactDTO;
import bynull.realty.web.json.ContactJSON;
import bynull.realty.web.json.PhoneNumberJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 3/6/15.
 */
@Component
public class ContactDtoJsonConverter implements Converter<ContactDTO, ContactJSON> {

    @Resource
    PhoneNumberDtoJsonConverter phoneNumberDtoJsonConverter;

    @Override
    public ContactJSON newTargetType(ContactDTO in) {
        return new ContactJSON();
    }

    @Override
    public ContactDTO newSourceType(ContactJSON in) {
        return new ContactDTO();
    }

    @Override
    public ContactJSON toTargetType(ContactDTO in, ContactJSON instance) {
        if(in == null) return null;
        instance.setId(in.getId());
        instance.setType(ContactJSON.Type.from(in.getType()));
        instance.setPhone(phoneNumberDtoJsonConverter.toTargetType(in.getPhoneNumber()).orElse(null));
        return instance;
    }

    @Override
    public ContactDTO toSourceType(ContactJSON in, ContactDTO instance) {
        throw new UnsupportedOperationException();
    }
}
