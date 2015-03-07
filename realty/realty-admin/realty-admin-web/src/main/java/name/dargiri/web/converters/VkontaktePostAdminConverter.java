package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.vk.VkontaktePostDTO;
import name.dargiri.web.form.VkontaktePostForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    public VkontaktePostForm newTargetType(VkontaktePostDTO in) {
        return new VkontaktePostForm();
    }

    @Override
    public VkontaktePostDTO newSourceType(VkontaktePostForm in) {
        return new VkontaktePostDTO();
    }

    @Override
    public VkontaktePostForm toTargetType(VkontaktePostDTO in, VkontaktePostForm form) {
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
        form.setPage(vkPageAdminConverter.toTargetType(in.getPage()));
        form.setMetros(metroAdminConverter.toTargetSet(in.getMetros()));
        form.setPhoneNumber(phoneNumberConverter.toTargetType(in.getPhoneNumber()));
        return form;
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
