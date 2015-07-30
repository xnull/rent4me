package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPageDTO;
import name.dargiri.web.form.FacebookPageForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPageAdminConverter implements Converter<FacebookPageDTO, FacebookPageForm> {

    @Resource
    CityAdminConverter cityConverter;

    @Override
    public FacebookPageForm newTargetType(Optional<FacebookPageDTO> in) {
        return new FacebookPageForm();
    }

    @Override
    public FacebookPageDTO newSourceType(FacebookPageForm in) {
        return new FacebookPageDTO();
    }

    @Override
    public Optional<FacebookPageForm> toTargetType(Optional<FacebookPageDTO> in, FacebookPageForm form) {
        return in.flatMap(fbDto -> {
            form.setId(fbDto.getId());
            form.setExternalId(fbDto.getExternalId());
            form.setLink(fbDto.getLink());
            form.setEnabled(fbDto.isEnabled());

            cityConverter.toTargetType(Optional.ofNullable(fbDto.getCity())).ifPresent(form::setCity);
            return Optional.of(form);
        });
    }

    @Override
    public FacebookPageDTO toSourceType(FacebookPageForm in, FacebookPageDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setExternalId(in.getExternalId());
        dto.setLink(in.getLink());
        dto.setEnabled(in.isEnabled());
        dto.setCity(cityConverter.toSourceType(in.getCity()));
        return dto;
    }

}
