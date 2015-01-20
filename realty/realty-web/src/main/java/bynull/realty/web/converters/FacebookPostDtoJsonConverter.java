package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPostDTO;
import bynull.realty.web.json.FacebookPostJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    public FacebookPostJSON toTargetType(FacebookPostDTO in) {
        if (in == null) {
            return null;
        }
        FacebookPostJSON json = new FacebookPostJSON();
        json.setId(in.getId());
        json.setLink(in.getLink());
        json.setMessage(in.getMessage());
        json.setRoomCount(in.getRoomCount());
        json.setRentalFee(in.getRentalFee());
        json.setPage(facebookPageConverter.toTargetType(in.getPage()));
        json.setImageUrls(in.getImageUrls());
        json.setCreated(in.getCreated());
        json.setUpdated(in.getUpdated());
        json.setMetros(metroConverter.toTargetSet(in.getMetros()));
        return json;
    }

    @Override
    public FacebookPostDTO toSourceType(FacebookPostJSON in) {
        throw new UnsupportedOperationException();
    }
}
