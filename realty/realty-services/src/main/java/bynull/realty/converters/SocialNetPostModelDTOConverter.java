package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.SocialNetPost;
import bynull.realty.dto.SocialNetPostDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Optional;

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
    public SocialNetPostDTO newTargetType(Optional<SocialNetPost> in) {
        return new SocialNetPostDTO();
    }

    @Override
    public SocialNetPost newSourceType(SocialNetPostDTO in) {
        return new SocialNetPost();
    }

    @Override
    public Optional<SocialNetPostDTO> toTargetType(Optional<SocialNetPost> post, SocialNetPostDTO dto) {
        return post.flatMap(p -> {
            dto.setId(p.getId().toStringRepresentation());
            dto.setLink(p.getLink());
            dto.setMessage(p.getMessage());
            dto.setRentalFee(p.getRentalFee());
            dto.setRoomCount(p.getRoomCount());
            dto.setCreated(p.getCreated());
            dto.setUpdated(p.getUpdated());
            dto.setSocialNetwork(p.getSocialNetwork());
            dto.setMetros(metroConverter.toTargetSet(p.getMetros()));
            dto.setPhoneNumberDTO(phoneNumberDTOConverter.toTargetType(p.getPhoneNumberOpt()).orElse(null));
//        dto.setMetros(Collections.emptySet());
//        dto.setPage(vkPageConverter.toTargetType(p.getVkontaktePage()));
            dto.setImageUrls(p.getPicture() != null ? Collections.singletonList(p.getPicture()) : Collections.emptyList());
            return Optional.of(dto);
        });
    }

    @Override
    public SocialNetPost toSourceType(SocialNetPostDTO in, SocialNetPost instance) {
        throw new UnsupportedOperationException();
    }
}
