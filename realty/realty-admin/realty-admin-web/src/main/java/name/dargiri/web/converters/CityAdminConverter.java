package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.vk.VkontaktePageDTO;
import name.dargiri.web.form.CityForm;
import name.dargiri.web.form.VkontaktePageForm;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class CityAdminConverter implements Converter<CityDTO, CityForm> {

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
        return form;
    }

    @Override
    public CityDTO toSourceType(CityForm in, CityDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setName(in.getName());
        return dto;
    }
}
