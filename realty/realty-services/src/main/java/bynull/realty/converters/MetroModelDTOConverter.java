package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.dto.GeoPointDTO;
import bynull.realty.dto.MetroDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class MetroModelDTOConverter implements Converter<MetroEntity, MetroDTO> {

    @Resource
    CityModelDTOConverter cityModelDTOConverter;

    @Override
    public MetroDTO newTargetType(Optional<MetroEntity> in) {
        return new MetroDTO();
    }

    @Override
    public MetroEntity newSourceType(MetroDTO in) {
        return new MetroEntity();
    }

    @Override
    public Optional<MetroDTO> toTargetType(Optional<MetroEntity> in, MetroDTO dto) {
        return in.flatMap(m -> {
            dto.setId(m.getId());
            dto.setStationName(m.getStationName());
            dto.setLocation(GeoPointDTO.from(m.getLocation()));
            dto.setCityOpt(cityModelDTOConverter.toTargetType(m.getCity()));
            return Optional.of(dto);
        });
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
