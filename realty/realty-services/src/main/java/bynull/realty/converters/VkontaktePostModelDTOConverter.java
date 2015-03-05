package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.facebook.FacebookScrapedPost;
import bynull.realty.data.business.external.vkontakte.VkontaktePost;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.dto.vk.VkontaktePostDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class VkontaktePostModelDTOConverter implements Converter<VkontaktePost, VkontaktePostDTO> {

    @Resource
    MetroModelDTOConverter metroConverter;

    @Resource
    VkontaktePageModelDTOConverter vkPageConverter;

    @Resource
    PhoneNumberModelDTOConverter phoneNumberConverter;

    @Override
    public VkontaktePostDTO newTargetType(VkontaktePost in) {
        return new VkontaktePostDTO();
    }

    @Override
    public VkontaktePost newSourceType(VkontaktePostDTO in) {
        return new VkontaktePost();
    }

    @Override
    public VkontaktePostDTO toTargetType(VkontaktePost post, VkontaktePostDTO dto) {
        if (post == null) return null;
        dto.setId(post.getId());
        dto.setLink(post.getLink());
        dto.setMessage(post.getMessage());
        dto.setRentalFee(post.getRentalFee());
        dto.setRoomCount(post.getRoomCount());
        dto.setCreated(post.getCreated());
        dto.setPhoneNumber(phoneNumberConverter.toTargetType(post.getPhoneNumber()));
        dto.setUpdated(post.getUpdated());
        dto.setMetros(metroConverter.toTargetSet(post.getMetros()));
        dto.setPage(vkPageConverter.toTargetType(post.getVkontaktePage()));
        dto.setImageUrls(post.getPicture() != null ? Collections.singletonList(post.getPicture()) : Collections.emptyList());
        return dto;
    }

    @Override
    public VkontaktePost toSourceType(VkontaktePostDTO in, VkontaktePost instance) {
        throw new UnsupportedOperationException();
    }
}
