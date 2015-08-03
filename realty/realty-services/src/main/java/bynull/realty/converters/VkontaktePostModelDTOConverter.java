package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.vkontakte.VkontaktePost;
import bynull.realty.dto.vk.VkontaktePostDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Optional;

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
    public VkontaktePostDTO newTargetType(Optional<VkontaktePost> in) {
        return new VkontaktePostDTO();
    }

    @Override
    public VkontaktePost newSourceType(VkontaktePostDTO in) {
        return new VkontaktePost();
    }

    @Override
    public Optional<VkontaktePostDTO> toTargetType(Optional<VkontaktePost> post, VkontaktePostDTO dto) {
        return post.flatMap(p -> {
            dto.setId(p.getId());
            dto.setLink(p.getLink());
            dto.setMessage(p.getMessage());
            dto.setRentalFee(p.getRentalFee());
            dto.setRoomCount(p.getRoomCount());
            dto.setCreated(p.getCreated());
            dto.setPhoneNumber(phoneNumberConverter.toTargetType(p.getPhoneNumberOpt()).orElse(null));
            dto.setUpdated(p.getUpdated());
            dto.setMetros(metroConverter.toTargetSet(p.getMetros()));
            dto.setPage(vkPageConverter.toTargetType(p.getVkontaktePageOpt()).orElse(null));
            dto.setImageUrls(p.getPicture() != null ? Collections.singletonList(p.getPicture()) : Collections.emptyList());
            return Optional.of(dto);
        });
    }

    @Override
    public VkontaktePost toSourceType(VkontaktePostDTO in, VkontaktePost instance) {
        throw new UnsupportedOperationException();
    }
}
