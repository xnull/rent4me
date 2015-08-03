package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.CountryDTO;
import name.dargiri.web.form.BoundingBoxForm;
import name.dargiri.web.form.CityForm;
import name.dargiri.web.form.CountryForm;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class CountryAdminConverter implements Converter<CountryDTO, CountryForm> {

    @Override
    public CountryForm newTargetType(Optional<CountryDTO> in) {
        return new CountryForm();
    }

    @Override
    public CountryDTO newSourceType(CountryForm in) {
        return new CountryDTO();
    }

    @Override
    public Optional<CountryForm> toTargetType(Optional<CountryDTO> in, CountryForm form) {
        return in.map(c ->  {
            form.setId(c.getId());
            form.setName(c.getName());
            return form;
        });
    }

    @Override
    public CountryDTO toSourceType(CountryForm in, CountryDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setName(in.getName());
        return dto;
    }
}
