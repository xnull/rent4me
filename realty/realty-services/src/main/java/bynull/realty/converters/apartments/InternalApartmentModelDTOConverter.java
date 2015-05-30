package bynull.realty.converters.apartments;

import bynull.realty.data.business.InternalApartment;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ApartmentPhotoDTO;
import bynull.realty.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class InternalApartmentModelDTOConverter extends BaseApartmentModelDTOConverter<InternalApartment> {
    @Override
    public ApartmentDTO toTargetType(InternalApartment apartment, ApartmentDTO dto) {
        ApartmentDTO result = super.toTargetType(apartment, dto);

        result.setOwner(UserDTO.from(apartment.getOwner()));

        result.setPhotos(apartment.listPhotosNewestFirst()
                        .stream()
                        .map(ApartmentPhotoDTO::from)
                        .collect(Collectors.toList())
        );

        return result;
    }
}
