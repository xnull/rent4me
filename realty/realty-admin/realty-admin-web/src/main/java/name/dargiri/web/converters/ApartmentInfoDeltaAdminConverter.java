package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.ApartmentInfoDeltaDTO;
import name.dargiri.web.form.AddressComponentsForm;
import name.dargiri.web.form.ApartmentInfoDeltaForm;
import name.dargiri.web.form.GeoPointForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 3/25/15.
 */
@Component
public class ApartmentInfoDeltaAdminConverter implements Converter<ApartmentInfoDeltaDTO, ApartmentInfoDeltaForm> {

    @Resource
    ApartmentAdminConverter apartmentAdminConverter;

    @Override
    public ApartmentInfoDeltaForm newTargetType(ApartmentInfoDeltaDTO in) {
        return new ApartmentInfoDeltaForm();
    }

    @Override
    public ApartmentInfoDeltaDTO newSourceType(ApartmentInfoDeltaForm in) {
        return new ApartmentInfoDeltaDTO();
    }

    @Override
    public ApartmentInfoDeltaForm toTargetType(ApartmentInfoDeltaDTO in, ApartmentInfoDeltaForm instance) {
        if (in == null) return null;

        instance.setId(in.getId());
        instance.setLocation(GeoPointForm.from(in.getLocation()));
        instance.setAddressComponents(AddressComponentsForm.from(in.getAddressComponents()));
        instance.setCreated(in.getCreated());
        instance.setUpdated(in.getUpdated());
        instance.setApplied(in.isApplied());
        instance.setRejected(in.isRejected());
        instance.setRoomCount(in.getRoomCount());
        instance.setFloorNumber(in.getFloorNumber());
        instance.setFloorsTotal(in.getFloorsTotal());
        instance.setArea(in.getArea());

        instance.setApartment(apartmentAdminConverter.toTargetType(in.getApartment()).orElse(null));

        return instance;
    }

    @Override
    public ApartmentInfoDeltaDTO toSourceType(ApartmentInfoDeltaForm in, ApartmentInfoDeltaDTO instance) {
        if (in == null) return null;
        instance.setId(in.getId());

        instance.setLocation(GeoPointForm.toDTO(in.getLocation()));
        instance.setAddressComponents(AddressComponentsForm.toDTO(in.getAddressComponents()));
        instance.setCreated(in.getCreated());
        instance.setUpdated(in.getUpdated());
        instance.setApplied(in.isApplied());
        instance.setRejected(in.isRejected());
        instance.setRoomCount(in.getRoomCount());
        instance.setFloorNumber(in.getFloorNumber());
        instance.setFloorsTotal(in.getFloorsTotal());
        instance.setArea(in.getArea());

        instance.setApartment(apartmentAdminConverter.toSourceType(in.getApartment()));

        return instance;
    }
}
