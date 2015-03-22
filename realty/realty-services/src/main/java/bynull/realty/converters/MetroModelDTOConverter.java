package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.dto.GeoPointDTO;
import bynull.realty.dto.MetroDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class MetroModelDTOConverter implements Converter<MetroEntity, MetroDTO> {

    @Resource
    CityModelDTOConverter cityModelDTOConverter;

    @Override
    public MetroDTO newTargetType(MetroEntity in) {
        return new MetroDTO();
    }

    @Override
    public MetroEntity newSourceType(MetroDTO in) {
        return new MetroEntity();
    }

    @Override
    public MetroDTO toTargetType(MetroEntity in, MetroDTO dto) {
        if (in == null) return null;
        dto.setId(in.getId());
        dto.setStationName(in.getStationName());
        dto.setLocation(GeoPointDTO.from(in.getLocation()));
        dto.setCity(cityModelDTOConverter.toTargetType(in.getCity()));
        return dto;
    }

    @Override
    public MetroEntity toSourceType(MetroDTO in, MetroEntity model) {
        if (in == null) return null;
        model.setId(in.getId());
        model.setStationName(in.getStationName());
        model.setLocation(GeoPointDTO.toInternal(in.getLocation()));
        model.setCity(cityModelDTOConverter.toSourceType(in.getCity()));
        return model;
    }

}
