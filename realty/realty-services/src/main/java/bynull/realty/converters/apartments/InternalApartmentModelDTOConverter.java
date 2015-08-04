package bynull.realty.converters.apartments;

import bynull.realty.data.business.InternalApartment;
import bynull.realty.data.business.User;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.ApartmentPhotoDTO;
import bynull.realty.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/5/15.
 */
@Component
public class InternalApartmentModelDTOConverter extends BaseApartmentModelDTOConverter<InternalApartment> {
    @Override
    public Optional<ApartmentDTO> toTargetType(Optional<InternalApartment> apartment, ApartmentDTO dto) {
        return apartment.flatMap(internalApartment ->
                super.toTargetType(apartment, dto).map(apt -> {
                    User owner = internalApartment.getOwner();
                    apt.setOwner(UserDTO.from(owner));

                    apt.setPhotos(apartment.get().listPhotosNewestFirst()
                                    .stream()
                                    .map(ApartmentPhotoDTO::from)
                                    .collect(Collectors.toList())
                    );

                    return apt;
                }));
    }
}
