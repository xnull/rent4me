package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.MetroDTO;
import bynull.realty.web.json.GeoPointJSON;
import bynull.realty.web.json.MetroJSON;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 20/01/15.
 */
@Component
public class MetroDtoJsonConverter implements Converter<MetroDTO, MetroJSON> {
    @Override
    public MetroJSON newTargetType(MetroDTO in) {
        return new MetroJSON();
    }

    @Override
    public MetroDTO newSourceType(MetroJSON in) {
        return new MetroDTO();
    }

    @Override
    public MetroJSON toTargetType(MetroDTO in, MetroJSON instance) {
        if (in == null) {
            return null;
        }
        MetroJSON json = new MetroJSON();
        json.setId(in.getId());
        json.setStationName(in.getStationName());
        json.setLocation(GeoPointJSON.from(in.getLocation()));
        return json;
    }

    @Override
    public MetroDTO toSourceType(MetroJSON in, MetroDTO dto) {
        if (in == null) {
            return null;
        }
        dto.setId(in.getId());
        dto.setStationName(in.getStationName());
        dto.setLocation(GeoPointJSON.toDTO(in.getLocation()));
        return dto;
    }
}
