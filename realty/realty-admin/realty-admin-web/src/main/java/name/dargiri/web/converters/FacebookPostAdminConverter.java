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

    @Resource
    private PhoneNumberAdminConverter phoneNumberConverter;

    @Override
    public FacebookPostForm newTargetType(FacebookPostDTO in) {
        return new FacebookPostForm();
    }

    @Override
    public FacebookPostDTO newSourceType(FacebookPostForm in) {
        return new FacebookPostDTO();
    }

    @Override
    public FacebookPostForm toTargetType(FacebookPostDTO in, FacebookPostForm form) {
        if (in == null) {
            return null;
        }
        form.setLink(in.getLink());
        form.setMessage(in.getMessage());
        form.setRoomCount(in.getRoomCount());
        form.setRentalFee(in.getRentalFee());
        form.setImageUrls(in.getImageUrls());
        form.setCreated(in.getCreated());
        form.setUpdated(in.getUpdated());
        form.setPhoneNumber(phoneNumberConverter.toTargetType(in.getPhoneNumberDTO()).orElse(null));
        form.setPage(facebookPageAdminConverter.toTargetType(in.getPage()).orElse(null));
        form.setMetros(metroAdminConverter.toTargetSet(in.getMetros()));
        return form;
    }

    @Override
    public FacebookPostDTO toSourceType(FacebookPostForm in, FacebookPostDTO dto) {
        if (in == null) return null;

        dto.setLink(in.getLink());
        dto.setMessage(in.getMessage());
        dto.setRoomCount(in.getRoomCount());
        dto.setRentalFee(in.getRentalFee());
        dto.setImageUrls(in.getImageUrls());
        dto.setCreated(in.getCreated());
        dto.setUpdated(in.getUpdated());
        dto.setPhoneNumberDTO(phoneNumberConverter.toSourceType(in.getPhoneNumber()));
        dto.setPage(facebookPageAdminConverter.toSourceType(in.getPage()));
        dto.setMetros(metroAdminConverter.toSourceSet(in.getMetros()));
        return dto;
    }
}
