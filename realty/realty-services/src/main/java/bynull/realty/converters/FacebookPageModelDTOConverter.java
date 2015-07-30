package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.dto.fb.FacebookPageDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * FacebookPageModelDTOConverter
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPageModelDTOConverter implements Converter<FacebookPageToScrap, FacebookPageDTO> {

    @Resource
    CityModelDTOConverter cityConverter;

    @Override
    public FacebookPageDTO newTargetType(Optional<FacebookPageToScrap> in) {
        return new FacebookPageDTO();
    }

    @Override
    public FacebookPageToScrap newSourceType(FacebookPageDTO in) {
        return new FacebookPageToScrap();
    }

    @Override
    public Optional<FacebookPageDTO> toTargetType(Optional<FacebookPageToScrap> in, FacebookPageDTO dto) {
        return in.flatMap(fbPage -> {
            dto.setId(fbPage.getId());
            dto.setLink(fbPage.getLink());
            dto.setExternalId(fbPage.getExternalId());
            dto.setEnabled(fbPage.isEnabled());
            cityConverter.toTargetType(Optional.ofNullable(fbPage.getCity())).ifPresent(dto::setCity);
            return Optional.of(dto);
        });
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
