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
    public MetroDTO toTargetType(MetroEntity in) {
        if (in == null) return null;
        MetroDTO dto = new MetroDTO();
        dto.setId(in.getId());
        dto.setStationName(in.getStationName());
        return dto;
    }

    @Override
    public MetroEntity toSourceType(MetroDTO in) {
        if (in == null) return null;
        MetroEntity model = new MetroEntity();
        model.setId(in.getId());
        model.setStationName(in.getStationName());
        return model;
    }
}
