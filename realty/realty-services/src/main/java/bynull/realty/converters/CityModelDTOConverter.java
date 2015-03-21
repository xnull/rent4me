package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.CityEntity;
import bynull.realty.dto.CityDTO;
import bynull.realty.dto.MetroDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class CityModelDTOConverter implements Converter<CityEntity, CityDTO> {

    @Override
    public CityDTO newTargetType(CityEntity in) {
        return new CityDTO();
    }

    @Override
    public CityEntity newSourceType(CityDTO in) {
        return new CityEntity();
    }

    @Override
    public CityDTO toTargetType(CityEntity in, CityDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setName(in.getName());
        return dto;
    }

    @Override
    public CityEntity toSourceType(CityDTO in, CityEntity model) {
        if (in == null) return null;
        model.setId(in.getId());
        model.setName(in.getName());
        return model;
    }

}
