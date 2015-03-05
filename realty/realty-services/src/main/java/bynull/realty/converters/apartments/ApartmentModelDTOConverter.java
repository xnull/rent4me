package bynull.realty.converters.apartments;

import bynull.realty.common.Converter;
import bynull.realty.data.business.Apartment;
import bynull.realty.dto.ApartmentDTO;

/**
 * Created by dionis on 3/5/15.
 */
public interface ApartmentModelDTOConverter<T extends Apartment> extends Converter<T, ApartmentDTO> {
}
