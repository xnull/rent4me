package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.facebook.FacebookScrapedPost;
import bynull.realty.dto.fb.FacebookPostDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

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
    public FacebookPostDTO newTargetType(FacebookScrapedPost in) {
        return new FacebookPostDTO();
    }

    @Override
    public FacebookScrapedPost newSourceType(FacebookPostDTO in) {
        return new FacebookScrapedPost();
    }

    @Override
    public FacebookPostDTO toTargetType(FacebookScrapedPost post, FacebookPostDTO dto) {
        if (post == null) return null;

        dto.setId(post.getId());
        dto.setLink(post.getLink());
        dto.setMessage(post.getMessage());
        dto.setRentalFee(post.getRentalFee());
        dto.setRoomCount(post.getRoomCount());
        dto.setCreated(post.getCreated());
        dto.setUpdated(post.getUpdated());
        dto.setMetros(metroConverter.toTargetSet(post.getMetros()));
        dto.setPage(facebookPageConverter.toTargetType(post.getFacebookPageToScrap()));
        dto.setPhoneNumberDTO(phoneNumberModelConverter.toTargetType(post.getPhoneNumber()));
        dto.setImageUrls(post.getPicture() != null ? Collections.singletonList(post.getPicture()) : Collections.emptyList());
        return dto;
    }


    @Override
    public FacebookScrapedPost toSourceType(FacebookPostDTO in, FacebookScrapedPost instance) {
        throw new UnsupportedOperationException();
    }
}
