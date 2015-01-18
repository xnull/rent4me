package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.fb.FacebookPageDTO;
import name.dargiri.web.form.FacebookPageForm;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class FacebookPageAdminConverter implements Converter<FacebookPageForm, FacebookPageDTO> {

    @Override
    public List<FacebookPageForm> toTargetList(Collection<? extends FacebookPageDTO> in) {
        return in.stream()
                .map(this::toTargetType)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacebookPageDTO> toSourceList(Collection<? extends FacebookPageForm> in) {
        return in.stream()
                .map(this::toSourceType)
                .collect(Collectors.toList());
    }

    @Override
    public FacebookPageForm toTargetType(FacebookPageDTO in) {
        FacebookPageForm form = new FacebookPageForm();
        form.setId(in.getId());
        form.setExternalId(in.getExternalId());
        form.setLink(in.getLink());
        return form;
    }

    @Override
    public FacebookPageDTO toSourceType(FacebookPageForm in) {
        FacebookPageDTO dto = new FacebookPageDTO();
        dto.setId(in.getId());
        dto.setExternalId(in.getExternalId());
        dto.setLink(in.getLink());
        return dto;
    }
}
