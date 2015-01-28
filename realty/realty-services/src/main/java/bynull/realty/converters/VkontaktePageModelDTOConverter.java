package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.vkontakte.AVkontaktePage;
import bynull.realty.dto.vk.VkontaktePageDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class VkontaktePageModelDTOConverter implements Converter<AVkontaktePage, VkontaktePageDTO> {

    @Override
    public VkontaktePageDTO toTargetType(AVkontaktePage in) {
        if (in == null) return null;
        VkontaktePageDTO dto = new VkontaktePageDTO();
        dto.setId(in.getId());
        dto.setLink(in.getLink());
        dto.setExternalId(in.getExternalId());
        return dto;
    }

    @Override
    public AVkontaktePage toSourceType(VkontaktePageDTO in) {
        if (in == null) return null;
        AVkontaktePage model = new AVkontaktePage();
        model.setId(in.getId());
        model.setExternalId(in.getExternalId());
        model.setLink(in.getLink());
        return model;
    }
}
