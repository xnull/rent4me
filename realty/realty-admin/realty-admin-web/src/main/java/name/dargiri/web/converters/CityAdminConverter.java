package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.vk.VkontaktePageDTO;
import name.dargiri.web.form.BoundingBoxForm;
import name.dargiri.web.form.CityForm;
import name.dargiri.web.form.VkontaktePageForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class CityAdminConverter implements Converter<CityDTO, CityForm> {

    @Resource
    CountryAdminConverter countryAdminConverter;

    @Override
    public CityForm newTargetType(CityDTO in) {
        return new CityForm();
    }

    @Override
    public CityDTO newSourceType(CityForm in) {
        return new CityDTO();
    }

    @Override
    public CityForm toTargetType(CityDTO in, CityForm form) {
        if (in == null) return null;
        form.setId(in.getId());
        form.setName(in.getName());
        form.setArea(BoundingBoxForm.from(in.getArea()));
        form.setCountry(countryAdminConverter.toTargetType(in.getCountry()));
        return form;
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
