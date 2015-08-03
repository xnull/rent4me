package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.PhoneNumberDTO;
import bynull.realty.dto.vk.VkontaktePostDTO;
import name.dargiri.web.form.PhoneNumberForm;
import name.dargiri.web.form.VkontaktePostForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class PhoneNumberAdminConverter implements Converter<PhoneNumberDTO, PhoneNumberForm> {


    @Override
    public PhoneNumberForm newTargetType(Optional<PhoneNumberDTO> in) {
        return new PhoneNumberForm();
    }

    @Override
    public PhoneNumberDTO newSourceType(PhoneNumberForm in) {
        return new PhoneNumberDTO();
    }

    @Override
    public Optional<PhoneNumberForm> toTargetType(Optional<PhoneNumberDTO> in, PhoneNumberForm form) {
        return in.map(p -> {
            form.setRaw(p.getRawNumber());
            form.setNationalFormattedNumber(p.getNationalFormattedNumber());
            return form;
        });
    }


    @Override
    public PhoneNumberDTO toSourceType(PhoneNumberForm in, PhoneNumberDTO dto) {
        if (in == null) return null;

        dto.setRawNumber(in.getRaw());
        dto.setNationalFormattedNumber(in.getNationalFormattedNumber());
        return dto;
    }

}
