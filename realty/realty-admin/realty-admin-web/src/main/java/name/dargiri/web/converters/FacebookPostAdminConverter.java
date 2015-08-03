package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPostDTO;
import name.dargiri.web.form.FacebookPostForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

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
    public FacebookPostForm newTargetType(Optional<FacebookPostDTO> in) {
        return new FacebookPostForm();
    }

    @Override
    public FacebookPostDTO newSourceType(FacebookPostForm in) {
        return new FacebookPostDTO();
    }

    @Override
    public Optional<FacebookPostForm> toTargetType(Optional<FacebookPostDTO> in, FacebookPostForm form) {
        return in.map(fb -> {
            form.setLink(fb.getLink());
            form.setMessage(fb.getMessage());
            form.setRoomCount(fb.getRoomCount());
            form.setRentalFee(fb.getRentalFee());
            form.setImageUrls(fb.getImageUrls());
            form.setCreated(fb.getCreated());
            form.setUpdated(fb.getUpdated());
            form.setPhoneNumber(phoneNumberConverter.toTargetType(fb.getPhoneOpt()).orElse(null));
            form.setPage(facebookPageAdminConverter.toTargetType(fb.getPageOpt()).orElse(null));
            form.setMetros(metroAdminConverter.toTargetSet(fb.getMetros()));
            return form;
        });
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
