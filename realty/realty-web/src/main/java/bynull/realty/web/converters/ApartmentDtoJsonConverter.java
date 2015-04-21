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

    @Resource
    CityDtoJsonConverter cityDtoJsonConverter;

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
        json.setDataSource(ApartmentJSON.DataSource.from(apartment.getDataSource()));

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
        List<ApartmentExternalPhotoJSON> imageUrls = apartment.getImageUrls() != null
                ? apartment.getImageUrls().stream().map(ApartmentExternalPhotoJSON::from).collect(Collectors.toList())
                : null;
        json.setImageUrls(imageUrls);

        List<? extends ContactJSON> contacts = contactDtoJsonConverter.toTargetList(apartment.getContacts());
        json.setContacts(contacts);

        json.setExternalLink(apartment.getExternalLink());
        json.setExternalAuthorLink(apartment.getExternalAuthorLink());
        json.setCity(apartment.getCity());

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
        dto.setLocation(GeoPointJSON.toDTO(in.getLocation()));
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
