package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import bynull.realty.dto.vk.VkontaktePageDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class VkontaktePageModelDTOConverter implements Converter<VkontaktePage, VkontaktePageDTO> {

    @Resource
    CityModelDTOConverter cityConverter;

    @Override
    public VkontaktePageDTO newTargetType(Optional<VkontaktePage> in) {
        return new VkontaktePageDTO();
    }

    @Override
    public VkontaktePage newSourceType(VkontaktePageDTO in) {
        return new VkontaktePage();
    }

    @Override
    public Optional<VkontaktePageDTO> toTargetType(Optional<VkontaktePage> in, VkontaktePageDTO dto) {
        return in.flatMap(vkPage -> {
            dto.setId(vkPage.getId());
            dto.setLink(vkPage.getLink());
            dto.setExternalId(vkPage.getExternalId());
            dto.setEnabled(vkPage.isEnabled());
            dto.setCity(cityConverter.toTargetType(vkPage.getCityOpt()).orElse(null));
            return Optional.of(dto);
        });
    }

    @Override
    public VkontaktePage toSourceType(VkontaktePageDTO in, VkontaktePage model) {
        if (in == null) return null;
        model.setId(in.getId());
        model.setExternalId(in.getExternalId());
        model.setLink(in.getLink());
        model.setEnabled(in.isEnabled());
        model.setCity(cityConverter.toSourceType(in.getCity()));
        return model;
    }
}
