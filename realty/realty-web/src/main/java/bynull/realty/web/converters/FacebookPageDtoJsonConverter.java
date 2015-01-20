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
    public FacebookPageJSON toTargetType(FacebookPageDTO in) {
        if (in == null) {
            return null;
        }
        FacebookPageJSON json = new FacebookPageJSON();
        json.setId(in.getId());
        json.setExternalId(in.getExternalId());
        json.setLink(in.getLink());
        return json;
    }

    @Override
    public FacebookPageDTO toSourceType(FacebookPageJSON in) {
        throw new UnsupportedOperationException();
    }
}
