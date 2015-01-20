package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPageDTO;
import name.dargiri.web.form.FacebookPageForm;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPageAdminConverter implements Converter<FacebookPageDTO, FacebookPageForm> {

    @Override
    public FacebookPageForm toTargetType(FacebookPageDTO in) {
        if (in == null) return null;
        FacebookPageForm form = new FacebookPageForm();
        form.setId(in.getId());
        form.setExternalId(in.getExternalId());
        form.setLink(in.getLink());
        return form;
    }

    @Override
    public FacebookPageDTO toSourceType(FacebookPageForm in) {
        if (in == null) return null;
        FacebookPageDTO dto = new FacebookPageDTO();
        dto.setId(in.getId());
        dto.setExternalId(in.getExternalId());
        dto.setLink(in.getLink());
        return dto;
    }
}
