package bynull.realty.converters.apartments;

import bynull.realty.data.business.*;
import bynull.realty.dto.*;
import bynull.realty.utils.HibernateUtil;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/5/15.
 */
public abstract class BaseApartmentModelDTOConverter<T extends Apartment> implements ApartmentModelDTOConverter<T> {
    @Override
    public ApartmentDTO toTargetType(T apartment, ApartmentDTO result) {
        if (apartment == null) return null;
        result.setId(apartment.getId());
        result.setLocation(GeoPointDTO.from(apartment.getLocation()));
        result.setAddress(AddressComponentsDTO.from(apartment.getAddressComponents()));
        result.setDescription(apartment.getDescription());
        result.setRoomCount(apartment.getRoomCount());
        result.setFloorNumber(apartment.getFloorNumber());
        result.setFloorsTotal(apartment.getFloorsTotal());
        result.setArea(apartment.getArea());

        result.setTypeOfRent(apartment.getTypeOfRent());
        result.setRentalFee(apartment.getRentalFee());
        result.setFeePeriod(apartment.getFeePeriod());

        result.setCreated(apartment.getCreated());
        result.setUpdated(apartment.getUpdated());
        result.setDataSource(apartment.getDataSource());
                result.setPublished(apartment.isPublished());

        return result;
    }

    @Override
    public T toSourceType(ApartmentDTO in, T instance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ApartmentDTO newTargetType(T in) {
        return new ApartmentDTO();
    }

    @Override
    public T newSourceType(ApartmentDTO in) {
        throw new UnsupportedOperationException();
    }
}
