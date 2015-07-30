package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.common.CountryEntity;
import bynull.realty.dto.CountryDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 18/01/15.
 */
@Component
public class CountryModelDTOConverter implements Converter<CountryEntity, CountryDTO> {

    @Override
    public CountryDTO newTargetType(Optional<CountryEntity> in) {
        return new CountryDTO();
    }

    @Override
    public CountryEntity newSourceType(CountryDTO in) {
        return new CountryEntity();
    }

    @Override
    public Optional<CountryDTO> toTargetType(Optional<CountryEntity> in, CountryDTO dto) {
        return in.flatMap(c -> {
            dto.setId(c.getId());
            dto.setName(c.getName());
            return Optional.of(dto);
        });
    }

    @Override
    public CountryEntity toSourceType(CountryDTO in, CountryEntity model) {
        if (in == null) return null;
        model.setId(in.getId());
        model.setName(in.getName());
        return model;
    }

}
