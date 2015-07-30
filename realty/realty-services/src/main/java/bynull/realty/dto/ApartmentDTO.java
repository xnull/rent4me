package bynull.realty.dto;

import bynull.realty.data.business.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 22/06/14.
 */
@Getter
@Setter
public class ApartmentDTO {
    private Long id;
    private GeoPointDTO location;
    private String city;
    private AddressComponentsDTO address;
    private String description;
    private Integer roomCount;
    private Integer floorNumber;
    private Integer floorsTotal;
    private BigDecimal area;
    private RentType typeOfRent;
    private BigDecimal rentalFee;
    private FeePeriod feePeriod;
    private Apartment.Target target;
    private Apartment.DataSource dataSource;

    private boolean published;
    private Date created;

    private Date updated;
    private List<? extends MetroDTO> metros;

    //internal specific
    private UserDTO owner;

    private List<ApartmentPhotoDTO> photos;
    private List<String> addedTempPhotoGUIDs;
    private List<String> deletePhotoGUIDs;

    /**
     * External specific.
     */
    private List<ApartmentExternalPhotoDTO> imageUrls;
    private String externalLink;
    private String externalAuthorLink;
    private String authorId;
    private List<? extends ContactDTO> contacts;

    public Date getCreated() {
        return copy(created);
    }

    public void setCreated(Date created) {
        this.created = copy(created);
    }

    public Date getUpdated() {
        return copy(updated);
    }

    public void setUpdated(Date updated) {
        this.updated = copy(updated);
    }

    public List<ApartmentPhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ApartmentPhotoDTO> photos) {
        this.photos = new ArrayList<>(photos);
    }

    public void setAddedTempPhotoGUIDs(List<String> addedTempPhotoGUIDs) {
        this.addedTempPhotoGUIDs = new ArrayList<>(addedTempPhotoGUIDs);
    }

    public void setDeletePhotoGUIDs(List<String> deletePhotoGUIDs) {
        this.deletePhotoGUIDs = new ArrayList<>(deletePhotoGUIDs);
    }

    //TODO: think about moving it to converter?
    public InternalApartment toInternal() {
        InternalApartment apartment = new InternalApartment();
        apartment.setId(getId());
        AddressComponentsDTO address = getAddress();
        apartment.setAddressComponents(AddressComponentsDTO.toInternal(address));
        apartment.setArea(getArea());
        apartment.setRoomCount(getRoomCount());
        apartment.setFloorNumber(getFloorNumber());
        apartment.setFloorsTotal(getFloorsTotal());
        apartment.setDescription(getDescription());
        apartment.setLocation(GeoPointDTO.toInternal(getLocation()));
        apartment.setCreated(getCreated());
        apartment.setUpdated(getUpdated());

        apartment.setTypeOfRent(getTypeOfRent());
        apartment.setRentalFee(getRentalFee());
        apartment.setFeePeriod(getFeePeriod());
        apartment.setPublished(isPublished());
        return apartment;
    }

    //TODO: think about moving it to converter?
    public ApartmentInfoDelta toApartmentInfoDelta() {
        ApartmentInfoDelta apartment = new ApartmentInfoDelta();

        AddressComponentsDTO address = getAddress();
        apartment.setAddressComponents(AddressComponentsDTO.toInternal(address));
        apartment.setArea(getArea());
        apartment.setRoomCount(getRoomCount());
        apartment.setFloorNumber(getFloorNumber());
        apartment.setFloorsTotal(getFloorsTotal());

        apartment.setLocation(GeoPointDTO.toInternal(getLocation()));

        return apartment;
    }
}
