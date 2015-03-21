package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPageDTO;
import name.dargiri.web.form.FacebookPageForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPageAdminConverter implements Converter<FacebookPageDTO, FacebookPageForm> {

    @Resource
    CityAdminConverter cityConverter;

    @Override
    public FacebookPageForm newTargetType(FacebookPageDTO in) {
        return  new FacebookPageForm();
    }

    @Override
    public FacebookPageDTO newSourceType(FacebookPageForm in) {
        return new FacebookPageDTO();
    }

    @Override
    public FacebookPageForm toTargetType(FacebookPageDTO in, FacebookPageForm form) {
        if (in == null) return null;
        form.setId(in.getId());
        form.setExternalId(in.getExternalId());
        form.setLink(in.getLink());
        form.setEnabled(in.isEnabled());
        form.setCity(cityConverter.toTargetType(in.getCity()));
        return form;
    }

    @Override
    public FacebookPageDTO toSourceType(FacebookPageForm in, FacebookPageDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setExternalId(in.getExternalId());
        dto.setLink(in.getLink());
        dto.setEnabled(in.isEnabled());
        dto.setCity(cityConverter.toSourceType(in.getCity()));
        return dto;
    }

}
