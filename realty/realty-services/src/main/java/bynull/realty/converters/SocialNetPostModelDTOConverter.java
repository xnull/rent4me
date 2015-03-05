package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.SocialNetPost;
import bynull.realty.dto.SocialNetPostDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class SocialNetPostModelDTOConverter implements Converter<SocialNetPost, SocialNetPostDTO> {

    @Resource
    MetroModelDTOConverter metroConverter;

    @Resource
    VkontaktePageModelDTOConverter vkPageConverter;

    @Resource
    PhoneNumberModelDTOConverter phoneNumberDTOConverter;

    @Override
    public SocialNetPostDTO newTargetType(SocialNetPost in) {
        return new SocialNetPostDTO();
    }

    @Override
    public SocialNetPost newSourceType(SocialNetPostDTO in) {
        return new SocialNetPost();
    }

    @Override
    public SocialNetPostDTO toTargetType(SocialNetPost post, SocialNetPostDTO dto) {
        if (post == null) return null;
        dto.setId(post.getId().toStringRepresentation());
        dto.setLink(post.getLink());
        dto.setMessage(post.getMessage());
        dto.setRentalFee(post.getRentalFee());
        dto.setRoomCount(post.getRoomCount());
        dto.setCreated(post.getCreated());
        dto.setUpdated(post.getUpdated());
        dto.setSocialNetwork(post.getSocialNetwork());
        dto.setMetros(metroConverter.toTargetSet(post.getMetros()));
        dto.setPhoneNumberDTO(phoneNumberDTOConverter.toTargetType(post.getPhoneNumber()));
//        dto.setMetros(Collections.emptySet());
//        dto.setPage(vkPageConverter.toTargetType(post.getVkontaktePage()));
        dto.setImageUrls(post.getPicture() != null ? Collections.singletonList(post.getPicture()) : Collections.emptyList());
        return dto;
    }

    @Override
    public SocialNetPost toSourceType(SocialNetPostDTO in, SocialNetPost instance) {
        throw new UnsupportedOperationException();
    }
}
