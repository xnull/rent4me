package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import bynull.realty.dto.vk.VkontaktePageDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class VkontaktePageModelDTOConverter implements Converter<VkontaktePage, VkontaktePageDTO> {

    @Override
    public VkontaktePageDTO toTargetType(VkontaktePage in) {
        if (in == null) return null;
        VkontaktePageDTO dto = new VkontaktePageDTO();
        dto.setId(in.getId());
        dto.setLink(in.getLink());
        dto.setExternalId(in.getExternalId());
        dto.setEnabled(in.isEnabled());
        return dto;
    }

    @Override
    public VkontaktePage toSourceType(VkontaktePageDTO in) {
        if (in == null) return null;
        VkontaktePage model = new VkontaktePage();
        model.setId(in.getId());
        model.setExternalId(in.getExternalId());
        model.setLink(in.getLink());
        model.setEnabled(in.isEnabled());
        return model;
    }
}
