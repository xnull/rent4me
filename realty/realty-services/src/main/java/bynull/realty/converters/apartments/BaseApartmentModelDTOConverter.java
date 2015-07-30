package bynull.realty.converters.apartments;

import bynull.realty.converters.MetroModelDTOConverter;
import bynull.realty.data.business.Apartment;
import bynull.realty.dto.AddressComponentsDTO;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.GeoPointDTO;
import bynull.realty.dto.MetroDTO;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Created by dionis on 3/5/15.
 */
public abstract class BaseApartmentModelDTOConverter<T extends Apartment> implements ApartmentModelDTOConverter<T> {

    @Resource
    MetroModelDTOConverter metroModelDTOConverter;

    @Override
    public Optional<ApartmentDTO> toTargetType(Optional<T> apartment, ApartmentDTO result) {
        return apartment.flatMap(ap -> {
            result.setId(ap.getId());
            result.setLocation(GeoPointDTO.from(ap.getLocation()));
            result.setAddress(AddressComponentsDTO.from(ap.getAddressComponents()));
            result.setDescription(ap.getDescription());
            result.setRoomCount(ap.getRoomCount());
            result.setFloorNumber(ap.getFloorNumber());
            result.setFloorsTotal(ap.getFloorsTotal());
            result.setArea(ap.getArea());

            result.setTypeOfRent(ap.getTypeOfRent());
            result.setRentalFee(ap.getRentalFee());
            result.setFeePeriod(ap.getFeePeriod());

            result.setCreated(ap.getLogicalCreated());
            result.setUpdated(ap.getUpdated());
            result.setDataSource(ap.getDataSource());
            result.setTarget(ap.getTarget());
            result.setPublished(ap.isPublished());

            List<? extends MetroDTO> metros = metroModelDTOConverter.toTargetList(ap.getMetros());
            result.setMetros(metros);

            return Optional.of(result);
        });
    }

    @Override
    public T toSourceType(ApartmentDTO in, T instance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ApartmentDTO newTargetType(Optional<T> in) {
        return new ApartmentDTO();
    }

    @Override
    public T newSourceType(ApartmentDTO in) {
        throw new UnsupportedOperationException();
    }
}
