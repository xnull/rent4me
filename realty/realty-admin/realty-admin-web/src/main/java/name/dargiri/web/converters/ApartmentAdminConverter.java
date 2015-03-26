package name.dargiri.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.MetroDTO;
import name.dargiri.web.form.AddressComponentsForm;
import name.dargiri.web.form.ApartmentForm;
import name.dargiri.web.form.GeoPointForm;
import name.dargiri.web.form.MetroForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dionis on 3/25/15.
 */
@Component
public class ApartmentAdminConverter implements Converter<ApartmentDTO, ApartmentForm> {

    @Resource
    MetroAdminConverter metroAdminConverter;

//    TODO: add later support
//    @Resource
//    ContactAdminConvereter contactAdminConvereter;

    @Override
    public ApartmentForm newTargetType(ApartmentDTO in) {
        return new ApartmentForm();
    }

    @Override
    public ApartmentDTO newSourceType(ApartmentForm in) {
        return new ApartmentDTO();
    }

    @Override
    public ApartmentForm toTargetType(ApartmentDTO in, ApartmentForm instance) {
        if (in == null) {
            return null;
        }


        instance.setId(in.getId());
        instance.setLocation(GeoPointForm.from(in.getLocation()));
        instance.setAddress(AddressComponentsForm.from(in.getAddress()));
        instance.setCreated(in.getCreated());
        instance.setUpdated(in.getUpdated());

        instance.setArea(in.getArea());
        instance.setRoomCount(in.getRoomCount());
        instance.setFloorNumber(in.getFloorNumber());
        instance.setFloorsTotal(in.getFloorsTotal());
        instance.setDescription(in.getDescription());

        instance.setTypeOfRent(in.getTypeOfRent());
        instance.setRentalFee(in.getRentalFee());
        instance.setFeePeriod(in.getFeePeriod());
        instance.setPublished(in.isPublished());
        instance.setDataSource(in.getDataSource());
        instance.setTarget(in.getTarget());

        List<? extends MetroForm> metros = metroAdminConverter.toTargetList(in.getMetros());
        instance.setMetros(metros);

        // internal specific


//        TODO: support this later
        /*
        instance.setPhotos(in.getPhotos() != null
                        ?   in.getPhotos()
                        .stream()
                        .map(ApartmentPhotoJSON::from)
                        .collect(Collectors.toList())
                        :   null
        );


        instance.setOwner(UserJSON.from(in.getOwner()));


        //social net specific
        List<ApartmentExternalPhotoJSON> imageUrls = in.getImageUrls() != null
                ? in.getImageUrls().stream().map(ApartmentExternalPhotoJSON::from).collect(Collectors.toList())
                : null;
        instance.setImageUrls(imageUrls);

        List<? extends ContactJSON> contacts = contactDtoJsonConverter.toTargetList(in.getContacts());
        instance.setContacts(contacts);
        */

        instance.setExternalLink(in.getExternalLink());
        instance.setExternalAuthorLink(in.getExternalAuthorLink());

        return instance;
    }

    @Override
    public ApartmentDTO toSourceType(ApartmentForm in, ApartmentDTO instance) {
        if (in == null) {
            return null;
        }


        instance.setId(in.getId());
        instance.setLocation(GeoPointForm.toDTO(in.getLocation()));
        instance.setAddress(AddressComponentsForm.toDTO(in.getAddress()));
        instance.setCreated(in.getCreated());
        instance.setUpdated(in.getUpdated());

        instance.setArea(in.getArea());
        instance.setRoomCount(in.getRoomCount());
        instance.setFloorNumber(in.getFloorNumber());
        instance.setFloorsTotal(in.getFloorsTotal());
        instance.setDescription(in.getDescription());

        instance.setTypeOfRent(in.getTypeOfRent());
        instance.setRentalFee(in.getRentalFee());
        instance.setFeePeriod(in.getFeePeriod());
        instance.setPublished(in.isPublished());
        instance.setDataSource(in.getDataSource());
        instance.setTarget(in.getTarget());

        List<? extends MetroDTO> metros = metroAdminConverter.toSourceList(in.getMetros());
        instance.setMetros(metros);

        // internal specific


//        TODO: support this later
        /*
        instance.setPhotos(in.getPhotos() != null
                        ?   in.getPhotos()
                        .stream()
                        .map(ApartmentPhotoJSON::from)
                        .collect(Collectors.toList())
                        :   null
        );


        instance.setOwner(UserJSON.from(in.getOwner()));


        //social net specific
        List<ApartmentExternalPhotoJSON> imageUrls = in.getImageUrls() != null
                ? in.getImageUrls().stream().map(ApartmentExternalPhotoJSON::from).collect(Collectors.toList())
                : null;
        instance.setImageUrls(imageUrls);

        List<? extends ContactJSON> contacts = contactDtoJsonConverter.toTargetList(in.getContacts());
        instance.setContacts(contacts);
        */

        instance.setExternalLink(in.getExternalLink());
        instance.setExternalAuthorLink(in.getExternalAuthorLink());

        return instance;
    }
}
