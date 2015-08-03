package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.facebook.FacebookScrapedPost;
import bynull.realty.dto.fb.FacebookPostDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPostModelDTOConverter implements Converter<FacebookScrapedPost, FacebookPostDTO> {

    @Resource
    MetroModelDTOConverter metroConverter;

    @Resource
    FacebookPageModelDTOConverter facebookPageConverter;

    @Resource
    PhoneNumberModelDTOConverter phoneNumberModelConverter;

    @Override
    public FacebookPostDTO newTargetType(Optional<FacebookScrapedPost> in) {
        return new FacebookPostDTO();
    }

    @Override
    public FacebookScrapedPost newSourceType(FacebookPostDTO in) {
        return new FacebookScrapedPost();
    }

    @Override
    public Optional<FacebookPostDTO> toTargetType(Optional<FacebookScrapedPost> post, FacebookPostDTO dto) {
        return post.flatMap(p -> {
            dto.setId(p.getId());
            dto.setLink(p.getLink());
            dto.setMessage(p.getMessage());
            dto.setRentalFee(p.getRentalFee());
            dto.setRoomCount(p.getRoomCount());
            dto.setCreated(p.getCreated());
            dto.setUpdated(p.getUpdated());
            dto.setMetros(metroConverter.toTargetSet(p.getMetros()));
            dto.setPage(facebookPageConverter.toTargetType(p.getFacebookPageToScrapOpt()).orElse(null));
            dto.setPhoneNumberDTO(phoneNumberModelConverter.toTargetType(p.getPhoneNumberOpt()).orElse(null));
            dto.setImageUrls(p.getPicture() != null ? Collections.singletonList(p.getPicture()) : Collections.emptyList());
            return Optional.of(dto);
        });
    }


    @Override
    public FacebookScrapedPost toSourceType(FacebookPostDTO in, FacebookScrapedPost instance) {
        throw new UnsupportedOperationException();
    }
}
