package bynull.realty.converters.contacts;

import bynull.realty.converters.PhoneNumberModelDTOConverter;
import bynull.realty.data.business.PhoneContact;
import bynull.realty.dto.ContactDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class PhoneContactModelDTOConverter extends BaseContactModelDTOConverter<PhoneContact> {

    @Resource
    PhoneNumberModelDTOConverter phoneNumberModelDTOConverter;

    @Override
    public ContactDTO toTargetType(PhoneContact in, ContactDTO instance) {
        if (in == null) return null;
        ContactDTO result = super.toTargetType(in, instance);

        result.setPhoneNumber(phoneNumberModelDTOConverter.toTargetType(in.getPhoneNumber()));

        return result;
    }
}
