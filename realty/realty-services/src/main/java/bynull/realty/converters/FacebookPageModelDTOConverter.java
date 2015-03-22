package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.dto.fb.FacebookPageDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPageModelDTOConverter implements Converter<FacebookPageToScrap, FacebookPageDTO> {

    @Resource
    CityModelDTOConverter cityConverter;

    @Override
    public FacebookPageDTO newTargetType(FacebookPageToScrap in) {
        return new FacebookPageDTO();
    }

    @Override
    public FacebookPageToScrap newSourceType(FacebookPageDTO in) {
        return new FacebookPageToScrap();
    }

    @Override
    public FacebookPageDTO toTargetType(FacebookPageToScrap in, FacebookPageDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setLink(in.getLink());
        dto.setExternalId(in.getExternalId());
        dto.setEnabled(in.isEnabled());
        dto.setCity(cityConverter.toTargetType(in.getCity()));
        return dto;
    }

    @Override
    public FacebookPageToScrap toSourceType(FacebookPageDTO in, FacebookPageToScrap model) {
        if (in == null) return null;
        model.setId(in.getId());
        model.setExternalId(in.getExternalId());
        model.setLink(in.getLink());
        model.setEnabled(in.isEnabled());
        model.setCity(cityConverter.toSourceType(in.getCity()));
        return model;
    }
}
