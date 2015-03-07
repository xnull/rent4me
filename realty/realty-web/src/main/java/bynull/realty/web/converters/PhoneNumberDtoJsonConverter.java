package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.PhoneNumberDTO;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.web.json.FacebookPostJSON;
import bynull.realty.web.json.PhoneNumberJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    public PhoneNumberJSON newTargetType(PhoneNumberDTO in) {
        return new PhoneNumberJSON();
    }

    @Override
    public PhoneNumberDTO newSourceType(PhoneNumberJSON in) {
        return new PhoneNumberDTO();
    }

    @Override
    public PhoneNumberJSON toTargetType(PhoneNumberDTO in, PhoneNumberJSON json) {
        if (in == null) {
            return null;
        }
        json.setRawNumber(in.getRawNumber());
        json.setNationalFormattedNumber(in.getNationalFormattedNumber());
        return json;
    }


    @Override
    public PhoneNumberDTO toSourceType(PhoneNumberJSON in, PhoneNumberDTO instance) {
        throw new UnsupportedOperationException();
    }
}
