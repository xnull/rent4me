package bynull.realty.converters.apartments;

import bynull.realty.common.Converter;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.ApartmentInfoDelta;
import bynull.realty.dto.AddressComponentsDTO;
import bynull.realty.dto.ApartmentInfoDeltaDTO;
import bynull.realty.dto.GeoPointDTO;
import bynull.realty.utils.HibernateUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 3/25/15.
 */
@Component
public class ApartmentInfoDeltaModelDTOConverter implements Converter<ApartmentInfoDelta, ApartmentInfoDeltaDTO> {

    @Resource
    ApartmentModelDTOConverterFactory<Apartment> apartmentModelDTOConverterFactory;

    @Override
    public ApartmentInfoDeltaDTO newTargetType(Optional<ApartmentInfoDelta> in) {
        return new ApartmentInfoDeltaDTO();
    }

    @Override
    public ApartmentInfoDelta newSourceType(ApartmentInfoDeltaDTO in) {
        return new ApartmentInfoDelta();
    }

    @Override
    public Optional<ApartmentInfoDeltaDTO> toTargetType(Optional<ApartmentInfoDelta> in, ApartmentInfoDeltaDTO instance) {
        return in.flatMap(aptDelta -> {
            instance.setId(aptDelta.getId());
            instance.setLocation(GeoPointDTO.from(aptDelta.getLocation()));
            instance.setAddressComponents(AddressComponentsDTO.from(aptDelta.getAddressComponents()));
            instance.setCreated(aptDelta.getCreated());
            instance.setUpdated(aptDelta.getUpdated());
            instance.setApplied(aptDelta.isApplied());
            instance.setRejected(aptDelta.isRejected());
            instance.setRoomCount(aptDelta.getRoomCount());
            instance.setFloorNumber(aptDelta.getFloorNumber());
            instance.setFloorsTotal(aptDelta.getFloorsTotal());
            instance.setArea(aptDelta.getArea());
            Apartment apartment = HibernateUtil.deproxy(aptDelta.getApartment());
            ApartmentModelDTOConverter<Apartment> targetConverter = apartmentModelDTOConverterFactory.getTargetConverter(apartment);
            instance.setApartment(targetConverter.toTargetType(Optional.ofNullable(apartment)).orElse(null));

            return Optional.of(instance);
        });
    }

    @Override
    public ApartmentInfoDelta toSourceType(ApartmentInfoDeltaDTO in, ApartmentInfoDelta instance) {
        if (in == null) return null;

        instance.setId(in.getId());
        instance.setLocation(GeoPointDTO.toInternal(in.getLocation()));
        instance.setAddressComponents(AddressComponentsDTO.toInternal(in.getAddressComponents()));
        instance.setCreated(in.getCreated());
        instance.setUpdated(in.getUpdated());
        instance.setApplied(in.isApplied());
        instance.setRejected(in.isRejected());
        instance.setRoomCount(in.getRoomCount());
        instance.setFloorNumber(in.getFloorNumber());
        instance.setFloorsTotal(in.getFloorsTotal());
        instance.setArea(in.getArea());
        ApartmentModelDTOConverter<Apartment> sourceConverter = apartmentModelDTOConverterFactory.getSourceConverter(in.getApartment());
        instance.setApartment(sourceConverter.toSourceType(in.getApartment()));

        return instance;
    }
}
