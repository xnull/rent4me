package bynull.realty.converters.apartments;

import bynull.realty.data.business.InternalApartment;
import bynull.realty.data.business.VkontakteApartment;
import bynull.realty.dto.ApartmentDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class VkontakteApartmentModelDTOConverter extends SocialNetApartmentModelDTOConverter<VkontakteApartment> {
    @Override
    public ApartmentDTO toTargetType(VkontakteApartment apartment, ApartmentDTO dto) {
        return super.toTargetType(apartment, dto);
    }
}
