package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.web.json.FacebookPostJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 20/01/15.
 */
@Component
public class FacebookPostDtoJsonConverter implements Converter<FacebookPostDTO, FacebookPostJSON> {

    @Resource
    MetroDtoJsonConverter metroConverter;

    @Resource
    FacebookPageDtoJsonConverter facebookPageConverter;

    @Override
    public FacebookPostJSON newTargetType(Optional<FacebookPostDTO> in) {
        return new FacebookPostJSON();
    }

    @Override
    public FacebookPostDTO newSourceType(FacebookPostJSON in) {
        return new FacebookPostDTO();
    }

    @Override
    public Optional<FacebookPostJSON> toTargetType(Optional<FacebookPostDTO> in, FacebookPostJSON json) {
        return in.map(fbPost -> {
            json.setId(fbPost.getId());
            json.setLink(fbPost.getLink());
            json.setMessage(fbPost.getMessage());
            json.setRoomCount(fbPost.getRoomCount());
            json.setRentalFee(fbPost.getRentalFee());
            json.setPage(facebookPageConverter.toTargetType(fbPost.getPageOpt()).orElse(null));
            json.setImageUrls(fbPost.getImageUrls());
            json.setCreated(fbPost.getCreated());
            json.setUpdated(fbPost.getUpdated());
            json.setMetros(metroConverter.toTargetSet(fbPost.getMetros()));
            return json;
        });
    }

    @Override
    public FacebookPostDTO toSourceType(FacebookPostJSON in, FacebookPostDTO instance) {
        throw new UnsupportedOperationException();
    }
}
