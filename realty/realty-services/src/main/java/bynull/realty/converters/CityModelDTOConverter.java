package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.common.CityEntity;
import bynull.realty.dto.BoundingBoxDTO;
import bynull.realty.dto.CityDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class CityModelDTOConverter implements Converter<CityEntity, CityDTO> {

    @Resource
    CountryModelDTOConverter countryModelDTOConverter;

    @Override
    public CityDTO newTargetType(Optional<CityEntity> in) {
        return new CityDTO();
    }

    @Override
    public CityEntity newSourceType(CityDTO in) {
        return new CityEntity();
    }

    @Override
    public Optional<CityDTO> toTargetType(Optional<CityEntity> in, CityDTO dto) {
        return in.flatMap(cityEntity -> {
            dto.setId(cityEntity.getId());
            dto.setName(cityEntity.getName());
            dto.setArea(BoundingBoxDTO.from(cityEntity.getArea()));
            countryModelDTOConverter.toTargetType(Optional.ofNullable(cityEntity.getCountry())).ifPresent(dto::setCountry);
            return Optional.of(dto);
        });
    }

    @Override
    public CityEntity toSourceType(CityDTO in, CityEntity model) {
        if (in == null) return null;
        model.setId(in.getId());
        model.setName(in.getName());
        model.setArea(BoundingBoxDTO.toInternal(in.getArea()));
        model.setCountry(countryModelDTOConverter.toSourceType(in.getCountry()));
        return model;
    }

}
