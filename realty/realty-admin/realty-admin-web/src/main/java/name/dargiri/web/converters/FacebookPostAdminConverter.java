package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPostDTO;
import name.dargiri.web.form.FacebookPostForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPostAdminConverter implements Converter<FacebookPostDTO, FacebookPostForm> {

    @Resource
    private FacebookPageAdminConverter facebookPageAdminConverter;

    @Resource
    private MetroAdminConverter metroAdminConverter;

    @Override
    public FacebookPostForm toTargetType(FacebookPostDTO in) {
        if (in == null) {
            return null;
        }
        FacebookPostForm form = new FacebookPostForm();
        form.setLink(in.getLink());
        form.setMessage(in.getMessage());
        form.setRoomCount(in.getRoomCount());
        form.setRentalFee(in.getRentalFee());
        form.setImageUrls(in.getImageUrls());
        form.setCreated(in.getCreated());
        form.setUpdated(in.getUpdated());
        form.setPage(facebookPageAdminConverter.toTargetType(in.getPage()));
        form.setMetros(metroAdminConverter.toTargetSet(in.getMetros()));
        return form;
    }

    @Override
    public FacebookPostDTO toSourceType(FacebookPostForm in) {
        if (in == null) return null;

        FacebookPostDTO dto = new FacebookPostDTO();
        dto.setLink(in.getLink());
        dto.setMessage(in.getMessage());
        dto.setRoomCount(in.getRoomCount());
        dto.setRentalFee(in.getRentalFee());
        dto.setImageUrls(in.getImageUrls());
        dto.setCreated(in.getCreated());
        dto.setUpdated(in.getUpdated());
        dto.setPage(facebookPageAdminConverter.toSourceType(in.getPage()));
        dto.setMetros(metroAdminConverter.toSourceSet(in.getMetros()));
        return dto;
    }
}
