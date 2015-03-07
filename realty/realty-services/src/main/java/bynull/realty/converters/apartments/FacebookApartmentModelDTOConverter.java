package bynull.realty.converters.apartments;

import bynull.realty.data.business.FacebookApartment;
import bynull.realty.data.business.InternalApartment;
import bynull.realty.dto.ApartmentDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class FacebookApartmentModelDTOConverter extends SocialNetApartmentModelDTOConverter<FacebookApartment> {
    @Override
    public ApartmentDTO toTargetType(FacebookApartment apartment, ApartmentDTO dto) {
        return super.toTargetType(apartment, dto);
    }
}
