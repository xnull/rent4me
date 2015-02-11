package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.PhoneNumber;
import bynull.realty.dto.PhoneNumberDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 2/11/15.
 */
@Component
public class PhoneNumberModelDTOConverter implements Converter<PhoneNumber, PhoneNumberDTO> {
    @Override
    public PhoneNumberDTO toTargetType(PhoneNumber in) {
        if (in == null) {
            return null;
        } else {
            PhoneNumberDTO dto = new PhoneNumberDTO();
            dto.setRawNumber(in.getRawNumber());
            dto.setNationalFormattedNumber(in.getNationalFormattedNumber());
            return dto;
        }
    }

    @Override
    public PhoneNumber toSourceType(PhoneNumberDTO in) {
        if (in == null) {
            return null;
        } else {
            PhoneNumber model = new PhoneNumber();
            model.setRawNumber(in.getRawNumber());
            model.setNationalFormattedNumber(in.getNationalFormattedNumber());
            return model;
        }
    }
}
