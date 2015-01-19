package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPostDTO;
import name.dargiri.web.form.FacebookPostForm;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPostAdminConverter implements Converter<FacebookPostForm, FacebookPostDTO> {

    @Override
    public FacebookPostForm toTargetType(FacebookPostDTO in) {
        if (in == null) {
            return null;
        }
        FacebookPostForm form = new FacebookPostForm();
        form.setLink(in.getLink());
        form.setMessage(in.getMessage());
        form.setImageUrls(in.getImageUrls());
        form.setCreated(in.getCreated());
        form.setUpdated(in.getUpdated());
        return form;
    }

    @Override
    public FacebookPostDTO toSourceType(FacebookPostForm in) {
        if (in == null) return null;

        FacebookPostDTO dto = new FacebookPostDTO();
        dto.setLink(in.getLink());
        dto.setMessage(in.getMessage());
        dto.setImageUrls(in.getImageUrls());
        dto.setCreated(in.getCreated());
        dto.setUpdated(in.getUpdated());
        return dto;
    }
}
