package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.web.json.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/6/15.
 */
@Component
public class ApartmentDtoJsonConverter implements Converter<ApartmentDTO, ApartmentJSON>{

    @Resource
    MetroDtoJsonConverter metroDtoJsonConverter;

    @Resource
    ContactDtoJsonConverter contactDtoJsonConverter;

    @Override
    public ApartmentJSON newTargetType(ApartmentDTO in) {
        return new ApartmentJSON();
    }

    @Override
    public ApartmentDTO newSourceType(ApartmentJSON in) {
        return new ApartmentDTO();
    }

    @Override
    public ApartmentJSON toTargetType(ApartmentDTO apartment, ApartmentJSON json) {
        if (apartment == null) return null;

        json.setId(apartment.getId());
        json.setLocation(GeoPointJSON.from(apartment.getLocation()));
        json.setAddress(AddressComponentsJSON.from(apartment.getAddress()));
        json.setCreated(apartment.getCreated());
        json.setUpdated(apartment.getUpdated());

        json.setArea(apartment.getArea());
        json.setRoomCount(apartment.getRoomCount());
        json.setFloorNumber(apartment.getFloorNumber());
        json.setFloorsTotal(apartment.getFloorsTotal());
        json.setDescription(apartment.getDescription());

        json.setTypeOfRent(apartment.getTypeOfRent());
        json.setRentalFee(apartment.getRentalFee());
        json.setFeePeriod(apartment.getFeePeriod());
        json.setPublished(apartment.isPublished());

        List<? extends MetroJSON> metros = metroDtoJsonConverter.toTargetList(apartment.getMetros());
        json.setMetros(metros);

        // internal specific

        json.setPhotos(apartment.getPhotos() != null
                        ?   apartment.getPhotos()
                        .stream()
                        .map(ApartmentPhotoJSON::from)
                        .collect(Collectors.toList())
                        :   null
        );


        json.setOwner(UserJSON.from(apartment.getOwner()));


        //social net specific
        json.setImageUrls(apartment.getImageUrls());

        List<? extends ContactJSON> contacts = contactDtoJsonConverter.toTargetList(apartment.getContacts());
        json.setContacts(contacts);

        return json;
    }

    @Override
    public ApartmentDTO toSourceType(ApartmentJSON in, ApartmentDTO dto) {
        if(in == null) return null;

        dto.setId(in.getId());
        AddressComponentsJSON address = in.getAddress();
        dto.setAddress(address != null ? address.toDTO() : null);
        dto.setCreated(in.getCreated());
        dto.setUpdated(in.getUpdated());
        dto.setLocation(in.getLocation() != null ? in.getLocation().toDTO(): null);
        dto.setArea(in.getArea());
        dto.setRoomCount(in.getRoomCount());
        dto.setFloorNumber(in.getFloorNumber());
        dto.setFloorsTotal(in.getFloorsTotal());
        dto.setDescription(in.getDescription());

        dto.setTypeOfRent(in.getTypeOfRent());
        dto.setRentalFee(in.getRentalFee());
        dto.setFeePeriod(in.getFeePeriod());

        dto.setAddedTempPhotoGUIDs(in.getAddedTempPhotoGUIDs());
        dto.setDeletePhotoGUIDs(in.getDeletePhotoGUIDs());

        dto.setPublished(in.isPublished());

        return dto;
    }
}
