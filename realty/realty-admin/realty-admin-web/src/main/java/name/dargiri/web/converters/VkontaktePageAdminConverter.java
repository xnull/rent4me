package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.vk.VkontaktePageDTO;
import name.dargiri.web.form.VkontaktePageForm;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class VkontaktePageAdminConverter implements Converter<VkontaktePageDTO, VkontaktePageForm> {

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
        return form;
    }

    @Override
    public VkontaktePageDTO toSourceType(VkontaktePageForm in, VkontaktePageDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setExternalId(in.getExternalId());
        dto.setLink(in.getLink());
        dto.setEnabled(in.isEnabled());
        return dto;
    }
}
