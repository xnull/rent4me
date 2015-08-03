package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.vk.VkontaktePostDTO;
import name.dargiri.web.form.VkontaktePostForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class VkontaktePostAdminConverter implements Converter<VkontaktePostDTO, VkontaktePostForm> {

    @Resource
    private VkontaktePageAdminConverter vkPageAdminConverter;

    @Resource
    private MetroAdminConverter metroAdminConverter;

    @Resource
    private PhoneNumberAdminConverter phoneNumberConverter;

    @Override
    public VkontaktePostForm newTargetType(Optional<VkontaktePostDTO> in) {
        return new VkontaktePostForm();
    }

    @Override
    public VkontaktePostDTO newSourceType(VkontaktePostForm in) {
        return new VkontaktePostDTO();
    }

    @Override
    public Optional<VkontaktePostForm> toTargetType(Optional<VkontaktePostDTO> in, VkontaktePostForm form) {
        return in.map(vkPost -> {
            form.setLink(vkPost.getLink());
            form.setMessage(vkPost.getMessage());
            form.setRoomCount(vkPost.getRoomCount());
            form.setRentalFee(vkPost.getRentalFee());
            form.setImageUrls(vkPost.getImageUrls());
            form.setCreated(vkPost.getCreated());
            form.setUpdated(vkPost.getUpdated());
            form.setPage(vkPageAdminConverter.toTargetType(vkPost.getPageOpt()).orElse(null));
            form.setMetros(metroAdminConverter.toTargetSet(vkPost.getMetros()));
            form.setPhoneNumber(phoneNumberConverter.toTargetType(vkPost.getPhoneNumberOpt()).orElse(null));
            return form;
        });
    }

    @Override
    public VkontaktePostDTO toSourceType(VkontaktePostForm in, VkontaktePostDTO dto) {
        if (in == null) return null;

        dto.setLink(in.getLink());
        dto.setMessage(in.getMessage());
        dto.setRoomCount(in.getRoomCount());
        dto.setRentalFee(in.getRentalFee());
        dto.setImageUrls(in.getImageUrls());
        dto.setCreated(in.getCreated());
        dto.setUpdated(in.getUpdated());
        dto.setPage(vkPageAdminConverter.toSourceType(in.getPage()));
        dto.setMetros(metroAdminConverter.toSourceSet(in.getMetros()));
        dto.setPhoneNumber(phoneNumberConverter.toSourceType(in.getPhoneNumber()));
        return dto;
    }

}
