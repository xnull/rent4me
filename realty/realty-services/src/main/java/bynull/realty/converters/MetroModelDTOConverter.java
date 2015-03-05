package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.dto.MetroDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class MetroModelDTOConverter implements Converter<MetroEntity, MetroDTO> {

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
        return dto;
    }

    @Override
    public MetroEntity toSourceType(MetroDTO in, MetroEntity model) {
        if (in == null) return null;
        model.setId(in.getId());
        model.setStationName(in.getStationName());
        return model;
    }

}
