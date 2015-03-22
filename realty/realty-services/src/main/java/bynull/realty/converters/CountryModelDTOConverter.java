package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.CountryEntity;
import bynull.realty.dto.BoundingBoxDTO;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.CountryDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class CountryModelDTOConverter implements Converter<CountryEntity, CountryDTO> {

    @Override
    public CountryDTO newTargetType(CountryEntity in) {
        return new CountryDTO();
    }

    @Override
    public CountryEntity newSourceType(CountryDTO in) {
        return new CountryEntity();
    }

    @Override
    public CountryDTO toTargetType(CountryEntity in, CountryDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setName(in.getName());
        return dto;
    }

    @Override
    public CountryEntity toSourceType(CountryDTO in, CountryEntity model) {
        if (in == null) return null;
        model.setId(in.getId());
        model.setName(in.getName());
        return model;
    }

}
