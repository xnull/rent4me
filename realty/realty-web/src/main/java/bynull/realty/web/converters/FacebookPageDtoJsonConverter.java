package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPageDTO;
import bynull.realty.web.json.FacebookPageJSON;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 20/01/15.
 */
@Component
public class FacebookPageDtoJsonConverter implements Converter<FacebookPageDTO, FacebookPageJSON> {
    @Override
    public FacebookPageJSON newTargetType(FacebookPageDTO in) {
        return new FacebookPageJSON();
    }

    @Override
    public FacebookPageDTO newSourceType(FacebookPageJSON in) {
        return new FacebookPageDTO();
    }

    @Override
    public FacebookPageJSON toTargetType(FacebookPageDTO in, FacebookPageJSON json) {
        if (in == null) {
            return null;
        }
        json.setId(in.getId());
        json.setExternalId(in.getExternalId());
        json.setLink(in.getLink());
        return json;
    }

    @Override
    public FacebookPageDTO toSourceType(FacebookPageJSON in, FacebookPageDTO instance) {
        throw new UnsupportedOperationException();
    }
}
