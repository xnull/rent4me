package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.dto.fb.FacebookPageDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPageModelDTOConverter implements Converter<FacebookPageDTO, FacebookPageToScrap> {
    @Override
    public List<FacebookPageDTO> toTargetList(Collection<? extends FacebookPageToScrap> in) {
        return in.stream().map(this::toTargetType).collect(Collectors.toList());
    }

    @Override
    public List<FacebookPageToScrap> toSourceList(Collection<? extends FacebookPageDTO> in) {
        return in.stream().map(this::toSourceType).collect(Collectors.toList());
    }

    @Override
    public FacebookPageDTO toTargetType(FacebookPageToScrap in) {
        if (in == null) return null;
        FacebookPageDTO dto = new FacebookPageDTO();
        dto.setId(in.getId());
        dto.setLink(in.getLink());
        dto.setExternalId(in.getExternalId());
        return dto;
    }

    @Override
    public FacebookPageToScrap toSourceType(FacebookPageDTO in) {
        if (in == null) return null;
        FacebookPageToScrap model = new FacebookPageToScrap();
        model.setId(in.getId());
        model.setExternalId(in.getExternalId());
        model.setLink(in.getLink());
        return model;
    }
}
