package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.SocialNetPostDTO;
import bynull.realty.web.json.SocialNetPostJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 20/01/15.
 */
@Component
public class SocialNetPostDtoJsonConverter implements Converter<SocialNetPostDTO, SocialNetPostJSON> {

    @Resource
    MetroDtoJsonConverter metroConverter;

    @Resource
    FacebookPageDtoJsonConverter facebookPageConverter;

    @Resource
    PhoneNumberDtoJsonConverter phoneNumberConverter;

    @Override
    public SocialNetPostJSON toTargetType(SocialNetPostDTO in) {
        if (in == null) {
            return null;
        }
        SocialNetPostJSON json = new SocialNetPostJSON();
        json.setId(in.getId());
        json.setLink(in.getLink());
        json.setMessage(in.getMessage());
        json.setRoomCount(in.getRoomCount());
        json.setRentalFee(in.getRentalFee());
//        json.setPage(facebookPageConverter.toTargetType(in.getPage()));
        json.setImageUrls(in.getImageUrls());
        json.setCreated(in.getCreated());
        json.setUpdated(in.getUpdated());
        json.setMetros(metroConverter.toTargetSet(in.getMetros()));
        json.setPhoneNumber(phoneNumberConverter.toTargetType(in.getPhoneNumberDTO()));
        json.setSocialNetwork(in.getSocialNetwork());
        return json;
    }

    @Override
    public SocialNetPostDTO toSourceType(SocialNetPostJSON in) {
        throw new UnsupportedOperationException();
    }
}
