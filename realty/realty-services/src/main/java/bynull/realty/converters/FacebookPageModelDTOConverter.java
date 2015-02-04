package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.dto.fb.FacebookPageDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPageModelDTOConverter implements Converter<FacebookPageToScrap, FacebookPageDTO> {

    @Override
    public FacebookPageDTO toTargetType(FacebookPageToScrap in) {
        if (in == null) return null;
        FacebookPageDTO dto = new FacebookPageDTO();
        dto.setId(in.getId());
        dto.setLink(in.getLink());
        dto.setExternalId(in.getExternalId());
        dto.setEnabled(in.isEnabled());
        return dto;
    }

    @Override
    public FacebookPageToScrap toSourceType(FacebookPageDTO in) {
        if (in == null) return null;
        FacebookPageToScrap model = new FacebookPageToScrap();
        model.setId(in.getId());
        model.setExternalId(in.getExternalId());
        model.setLink(in.getLink());
        model.setEnabled(in.isEnabled());
        return model;
    }
}
