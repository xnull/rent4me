package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.MetroDTO;
import bynull.realty.web.json.MetroJSON;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 20/01/15.
 */
@Component
public class MetroDtoJsonConverter implements Converter<MetroDTO, MetroJSON> {
    @Override
    public MetroJSON toTargetType(MetroDTO in) {
        if (in == null) {
            return null;
        }
        MetroJSON json = new MetroJSON();
        json.setId(in.getId());
        json.setStationName(in.getStationName());
        return json;
    }

    @Override
    public MetroDTO toSourceType(MetroJSON in) {
        if (in == null) {
            return null;
        }
        MetroDTO dto = new MetroDTO();
        dto.setId(dto.getId());
        dto.setStationName(dto.getStationName());
        return dto;
    }
}
