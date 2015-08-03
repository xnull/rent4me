package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.PhoneNumberDTO;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.web.json.FacebookPostJSON;
import bynull.realty.web.json.PhoneNumberJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 20/01/15.
 */
@Component
public class PhoneNumberDtoJsonConverter implements Converter<PhoneNumberDTO, PhoneNumberJSON> {

    @Resource
    MetroDtoJsonConverter metroConverter;

    @Resource
    FacebookPageDtoJsonConverter facebookPageConverter;

    @Override
    public PhoneNumberJSON newTargetType(Optional<PhoneNumberDTO> in) {
        return new PhoneNumberJSON();
    }

    @Override
    public PhoneNumberDTO newSourceType(PhoneNumberJSON in) {
        return new PhoneNumberDTO();
    }

    @Override
    public Optional<PhoneNumberJSON> toTargetType(Optional<PhoneNumberDTO> in, PhoneNumberJSON json) {
        return in.map(p ->{
            json.setRawNumber(p.getRawNumber());
            json.setNationalFormattedNumber(p.getNationalFormattedNumber());
            return json;
        });
    }


    @Override
    public PhoneNumberDTO toSourceType(PhoneNumberJSON in, PhoneNumberDTO instance) {
        throw new UnsupportedOperationException();
    }
}
