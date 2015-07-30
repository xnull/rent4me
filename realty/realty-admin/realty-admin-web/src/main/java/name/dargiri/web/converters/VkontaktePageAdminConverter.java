package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.vk.VkontaktePageDTO;
import name.dargiri.web.form.VkontaktePageForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class VkontaktePageAdminConverter implements Converter<VkontaktePageDTO, VkontaktePageForm> {

    @Resource
    CityAdminConverter cityConverter;

    @Override
    public VkontaktePageForm newTargetType(VkontaktePageDTO in) {
        return new VkontaktePageForm();
    }

    @Override
    public VkontaktePageDTO newSourceType(VkontaktePageForm in) {
        return new VkontaktePageDTO();
    }

    @Override
    public VkontaktePageForm toTargetType(VkontaktePageDTO in, VkontaktePageForm form) {
        if (in == null) return null;
        form.setId(in.getId());
        form.setExternalId(in.getExternalId());
        form.setLink(in.getLink());
        form.setEnabled(in.isEnabled());
        form.setCity(cityConverter.toTargetType(in.getCity()).orElse(null));
        return form;
    }

    @Override
    public VkontaktePageDTO toSourceType(VkontaktePageForm in, VkontaktePageDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setExternalId(in.getExternalId());
        dto.setLink(in.getLink());
        dto.setEnabled(in.isEnabled());
        dto.setCity(cityConverter.toSourceType(in.getCity()));
        return dto;
    }
}
