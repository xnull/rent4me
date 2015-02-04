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

    @Override
    public VkontaktePostForm toTargetType(VkontaktePostDTO in) {
        if (in == null) {
            return null;
        }
        VkontaktePostForm form = new VkontaktePostForm();
        form.setLink(in.getLink());
        form.setMessage(in.getMessage());
        form.setRoomCount(in.getRoomCount());
        form.setRentalFee(in.getRentalFee());
        form.setImageUrls(in.getImageUrls());
        form.setCreated(in.getCreated());
        form.setUpdated(in.getUpdated());
        form.setPage(vkPageAdminConverter.toTargetType(in.getPage()));
        form.setMetros(metroAdminConverter.toTargetSet(in.getMetros()));
        return form;
    }

    @Override
    public VkontaktePostDTO toSourceType(VkontaktePostForm in) {
        if (in == null) return null;

        VkontaktePostDTO dto = new VkontaktePostDTO();
        dto.setLink(in.getLink());
        dto.setMessage(in.getMessage());
        dto.setRoomCount(in.getRoomCount());
        dto.setRentalFee(in.getRentalFee());
        dto.setImageUrls(in.getImageUrls());
        dto.setCreated(in.getCreated());
        dto.setUpdated(in.getUpdated());
        dto.setPage(vkPageAdminConverter.toSourceType(in.getPage()));
        dto.setMetros(metroAdminConverter.toSourceSet(in.getMetros()));
        return dto;
    }
}