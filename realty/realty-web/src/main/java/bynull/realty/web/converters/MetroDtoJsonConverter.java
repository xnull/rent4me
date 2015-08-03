package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.MetroDTO;
import bynull.realty.web.json.GeoPointJSON;
import bynull.realty.web.json.MetroJSON;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 20/01/15.
 */
@Component
public class MetroDtoJsonConverter implements Converter<MetroDTO, MetroJSON> {
    @Override
    public MetroJSON newTargetType(Optional<MetroDTO> in) {
        return new MetroJSON();
    }

    @Override
    public MetroDTO newSourceType(MetroJSON in) {
        return new MetroDTO();
    }

    @Override
    public Optional<MetroJSON> toTargetType(Optional<MetroDTO> in, MetroJSON instance) {
        return in.map(metro -> {
            MetroJSON json = new MetroJSON();
            json.setId(metro.getId());
            json.setStationName(metro.getStationName());
            json.setLocation(GeoPointJSON.from(metro.getLocation()));
            return json;
        });
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
