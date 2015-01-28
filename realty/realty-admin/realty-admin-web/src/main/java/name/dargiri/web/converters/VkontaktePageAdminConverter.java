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
    public VkontaktePageForm toTargetType(VkontaktePageDTO in) {
        if (in == null) return null;
        VkontaktePageForm form = new VkontaktePageForm();
        form.setId(in.getId());
        form.setExternalId(in.getExternalId());
        form.setLink(in.getLink());
        return form;
    }

    @Override
    public VkontaktePageDTO toSourceType(VkontaktePageForm in) {
        if (in == null) return null;
        VkontaktePageDTO dto = new VkontaktePageDTO();
        dto.setId(in.getId());
        dto.setExternalId(in.getExternalId());
        dto.setLink(in.getLink());
        return dto;
    }
}
