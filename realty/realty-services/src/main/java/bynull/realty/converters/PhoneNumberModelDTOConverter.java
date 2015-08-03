package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.PhoneNumber;
import bynull.realty.dto.PhoneNumberDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 2/11/15.
 */
@Component
public class PhoneNumberModelDTOConverter implements Converter<PhoneNumber, PhoneNumberDTO> {
    @Override
    public PhoneNumberDTO newTargetType(Optional<PhoneNumber> in) {
        return new PhoneNumberDTO();
    }

    @Override
    public PhoneNumber newSourceType(PhoneNumberDTO in) {
        return new PhoneNumber();
    }

    @Override
    public Optional<PhoneNumberDTO> toTargetType(Optional<PhoneNumber> in, PhoneNumberDTO dto) {
        return in.flatMap(p -> {
            dto.setRawNumber(p.getRawNumber());
            dto.setNationalFormattedNumber(p.getNationalFormattedNumber());
            return Optional.of(dto);
        });
    }

    @Override
    public PhoneNumber toSourceType(PhoneNumberDTO in, PhoneNumber model) {
        if (in == null) {
            return null;
        } else {
            model.setRawNumber(in.getRawNumber());
            model.setNationalFormattedNumber(in.getNationalFormattedNumber());
            return model;
        }
    }
}
