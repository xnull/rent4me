package bynull.realty.converters.contacts;

import bynull.realty.converters.PhoneNumberModelDTOConverter;
import bynull.realty.data.business.PhoneContact;
import bynull.realty.dto.ContactDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class PhoneContactModelDTOConverter extends BaseContactModelDTOConverter<PhoneContact> {

    @Resource
    PhoneNumberModelDTOConverter phoneNumberModelDTOConverter;

    @Override
    public Optional<ContactDTO> toTargetType(Optional<PhoneContact> in, ContactDTO instance) {
        return in.flatMap(p -> {
            ContactDTO result = super.toTargetType(in, instance).get();
            phoneNumberModelDTOConverter.toTargetType(p.getPhoneNumberOpt()).ifPresent(result::setPhoneNumber);
            return Optional.of(result);
        });
    }
}
