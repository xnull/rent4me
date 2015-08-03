package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPageDTO;
import bynull.realty.web.json.FacebookPageJSON;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 20/01/15.
 */
@Component
public class FacebookPageDtoJsonConverter implements Converter<FacebookPageDTO, FacebookPageJSON> {
    @Override
    public FacebookPageJSON newTargetType(Optional<FacebookPageDTO> in) {
        return new FacebookPageJSON();
    }

    @Override
    public FacebookPageDTO newSourceType(FacebookPageJSON in) {
        return new FacebookPageDTO();
    }

    @Override
    public Optional<FacebookPageJSON> toTargetType(Optional<FacebookPageDTO> in, FacebookPageJSON json) {
        return in.flatMap(f -> {
            json.setId(f.getId());
            json.setExternalId(f.getExternalId());
            json.setLink(f.getLink());
            return Optional.of(json);
        });

    }

    @Override
    public FacebookPageDTO toSourceType(FacebookPageJSON in, FacebookPageDTO instance) {
        throw new UnsupportedOperationException();
    }
}
