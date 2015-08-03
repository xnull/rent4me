package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.CityDTO;
import bynull.realty.web.json.BoundingBoxJSON;
import bynull.realty.web.json.CityJSON;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 20/01/15.
 */
@Component
public class CityDtoJsonConverter implements Converter<CityDTO, CityJSON> {
    @Override
    public CityJSON newTargetType(Optional<CityDTO> in) {
        return null;
    }

    @Override
    public CityDTO newSourceType(CityJSON in) {
        return null;
    }

    @Override
    public Optional<CityJSON> toTargetType(Optional<CityDTO> in, CityJSON instance) {
        return in.flatMap(c -> {
            CityJSON json = new CityJSON();
            json.setId(c.getId());
            json.setArea(BoundingBoxJSON.from(c.getArea()));
            json.setName(c.getName());
            return Optional.of(json);
        });
    }

    @Override
    public CityDTO toSourceType(CityJSON in, CityDTO dto) {
        if (in == null) {
            return null;
        }
        dto.setId(in.getId());
        dto.setArea(BoundingBoxJSON.toDTO(in.getArea()));
        dto.setName(in.getName());
        return dto;
    }
}
