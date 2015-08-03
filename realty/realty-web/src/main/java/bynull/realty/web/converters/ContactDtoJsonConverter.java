package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.ContactDTO;
import bynull.realty.web.json.ContactJSON;
import bynull.realty.web.json.PhoneNumberJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 3/6/15.
 */
@Component
public class ContactDtoJsonConverter implements Converter<ContactDTO, ContactJSON> {

    @Resource
    PhoneNumberDtoJsonConverter phoneNumberDtoJsonConverter;

    @Override
    public ContactJSON newTargetType(Optional<ContactDTO> in) {
        return new ContactJSON();
    }

    @Override
    public ContactDTO newSourceType(ContactJSON in) {
        return new ContactDTO();
    }

    @Override
    public Optional<ContactJSON> toTargetType(Optional<ContactDTO> in, ContactJSON instance) {
        return in.map(c -> {
            instance.setId(c.getId());
            instance.setType(ContactJSON.Type.from(c.getType()));
            instance.setPhone(phoneNumberDtoJsonConverter.toTargetType(c.getPhoneNumberOpt()).orElse(null));
            return instance;
        });
    }

    @Override
    public ContactDTO toSourceType(ContactJSON in, ContactDTO instance) {
        throw new UnsupportedOperationException();
    }
}
