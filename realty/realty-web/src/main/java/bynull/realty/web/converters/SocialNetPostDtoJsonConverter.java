package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.SocialNetPostDTO;
import bynull.realty.web.json.SocialNetPostJSON;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

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
    public SocialNetPostJSON newTargetType(Optional<SocialNetPostDTO> in) {
        return new SocialNetPostJSON();
    }

    @Override
    public SocialNetPostDTO newSourceType(SocialNetPostJSON in) {
        return new SocialNetPostDTO();
    }

    @Override
    public Optional<SocialNetPostJSON> toTargetType(Optional<SocialNetPostDTO> in, SocialNetPostJSON json) {
        return in.map(sNet -> {
            json.setId(sNet.getId());
            json.setLink(sNet.getLink());
            json.setMessage(sNet.getMessage());
            json.setRoomCount(sNet.getRoomCount());
            json.setRentalFee(sNet.getRentalFee());
//        json.setPage(facebookPageConverter.toTargetType(sNet.getPage()));
            json.setImageUrls(sNet.getImageUrls());
            json.setCreated(sNet.getCreated());
            json.setUpdated(sNet.getUpdated());
            json.setMetros(metroConverter.toTargetSet(sNet.getMetros()));
            json.setPhoneNumber(phoneNumberConverter.toTargetType(sNet.getPhoneNumberOpt()).orElse(null));
            json.setSocialNetwork(sNet.getSocialNetwork());
            return json;
        });
    }

    @Override
    public SocialNetPostDTO toSourceType(SocialNetPostJSON in, SocialNetPostDTO instance) {
        throw new UnsupportedOperationException();
    }
}
