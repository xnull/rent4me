package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.CityDTO;
import bynull.realty.web.json.BoundingBoxJSON;
import bynull.realty.web.json.CityJSON;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 20/01/15.
 */
@Component
public class CityDtoJsonConverter implements Converter<CityDTO, CityJSON> {
    @Override
    public CityJSON newTargetType(CityDTO in) {
        return null;
    }

    @Override
    public CityDTO newSourceType(CityJSON in) {
        return null;
    }

    @Override
    public CityJSON toTargetType(CityDTO in, CityJSON instance) {
        if (in == null) {
            return null;
        }
        CityJSON json = new CityJSON();
        json.setId(in.getId());
        json.setArea(BoundingBoxJSON.from(in.getArea()));
        json.setName(in.getName());
        return json;
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
