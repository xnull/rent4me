package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.vk.VkontaktePageDTO;
import name.dargiri.web.form.BoundingBoxForm;
import name.dargiri.web.form.CityForm;
import name.dargiri.web.form.VkontaktePageForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class CityAdminConverter implements Converter<CityDTO, CityForm> {

    @Resource
    CountryAdminConverter countryAdminConverter;

    @Override
    public CityForm newTargetType(Optional<CityDTO> in) {
        return new CityForm();
    }

    @Override
    public CityDTO newSourceType(CityForm in) {
        return new CityDTO();
    }

    @Override
    public Optional<CityForm> toTargetType(Optional<CityDTO> in, CityForm form) {
        return in.flatMap(cityDTO -> {
            form.setId(cityDTO.getId());
            form.setName(cityDTO.getName());
            form.setArea(BoundingBoxForm.from(cityDTO.getArea()));
            countryAdminConverter.toTargetType(Optional.ofNullable(cityDTO.getCountry())).ifPresent(form::setCountry);
            return Optional.of(form);
        });
    }

    @Override
    public CityDTO toSourceType(CityForm in, CityDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setName(in.getName());
        dto.setArea(BoundingBoxForm.toDTO(in.getArea()));
        dto.setCountry(countryAdminConverter.toSourceType(in.getCountry()));
        return dto;
    }
}
