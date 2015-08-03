package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.web.json.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
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
    public ApartmentJSON newTargetType(Optional<ApartmentDTO> in) {
        return new ApartmentJSON();
    }

    @Override
    public ApartmentDTO newSourceType(ApartmentJSON in) {
        return new ApartmentDTO();
    }

    @Override
    public Optional<ApartmentJSON> toTargetType(Optional<ApartmentDTO> apartment, ApartmentJSON json) {
        return apartment.map(apt -> {
            json.setId(apt.getId());
            json.setLocation(GeoPointJSON.from(apt.getLocation()));
            json.setAddress(AddressComponentsJSON.from(apt.getAddress()));
            json.setCreated(apt.getCreated());
            json.setUpdated(apt.getUpdated());

            json.setArea(apt.getArea());
            json.setRoomCount(apt.getRoomCount());
            json.setFloorNumber(apt.getFloorNumber());
            json.setFloorsTotal(apt.getFloorsTotal());
            json.setDescription(apt.getDescription());

            json.setTypeOfRent(apt.getTypeOfRent());
            json.setRentalFee(apt.getRentalFee());
            json.setFeePeriod(apt.getFeePeriod());
            json.setPublished(apt.isPublished());
            json.setDataSource(ApartmentJSON.DataSource.from(apt.getDataSource()));

            List<? extends MetroJSON> metros = metroDtoJsonConverter.toTargetList(apt.getMetros());
            json.setMetros(metros);

            // internal specific

            json.setPhotos(apt.getPhotos() != null
                            ?   apt.getPhotos()
                            .stream()
                            .map(ApartmentPhotoJSON::from)
                            .collect(Collectors.toList())
                            :   null
            );


            json.setOwner(UserJSON.from(apt.getOwner()));


            //social net specific
            List<ApartmentExternalPhotoJSON> imageUrls = apt.getImageUrls() != null
                    ? apt.getImageUrls().stream().map(ApartmentExternalPhotoJSON::from).collect(Collectors.toList())
                    : null;
            json.setImageUrls(imageUrls);

            List<? extends ContactJSON> contacts = contactDtoJsonConverter.toTargetList(apt.getContacts());
            json.setContacts(contacts);

            json.setExternalLink(apt.getExternalLink());
            json.setExternalAuthorLink(apt.getExternalAuthorLink());
            json.setCity(apt.getCity());

            return json;
        });
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
