package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.PhoneNumberDTO;
import bynull.realty.dto.vk.VkontaktePostDTO;
import name.dargiri.web.form.PhoneNumberForm;
import name.dargiri.web.form.VkontaktePostForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class PhoneNumberAdminConverter implements Converter<PhoneNumberDTO, PhoneNumberForm> {


    @Override
    public PhoneNumberForm toTargetType(PhoneNumberDTO in) {
        if (in == null) {
            return null;
        }
        PhoneNumberForm form = new PhoneNumberForm();
        form.setRaw(in.getRawNumber());
        form.setNationalFormattedNumber(in.getNationalFormattedNumber());
        return form;
    }

    @Override
    public PhoneNumberDTO toSourceType(PhoneNumberForm in) {
        if (in == null) return null;

        PhoneNumberDTO dto = new PhoneNumberDTO();
        dto.setRawNumber(in.getRaw());
        dto.setNationalFormattedNumber(in.getNationalFormattedNumber());
        return dto;
    }
}
