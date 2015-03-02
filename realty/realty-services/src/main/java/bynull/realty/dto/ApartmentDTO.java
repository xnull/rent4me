package bynull.realty.dto;

import bynull.realty.data.business.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private boolean published;

    private Date created;
    private Date updated;

    private List<ApartmentPhotoDTO> photos = Collections.emptyList();
    private List<String> addedTempPhotoGUIDs = Collections.emptyList();
    private List<String> deletePhotoGUIDs = Collections.emptyList();
    private UserDTO owner;

    public static ApartmentDTO from(InternalApartment apartment) {
        if (apartment == null) return null;
        ApartmentDTO dto = new ApartmentDTO();
        dto.setId(apartment.getId());
        dto.setLocation(GeoPointDTO.from(apartment.getLocation()));
        dto.setAddress(AddressComponentsDTO.from(apartment.getAddressComponents()));
        dto.setDescription(apartment.getDescription());
        dto.setRoomCount(apartment.getRoomCount());
        dto.setFloorNumber(apartment.getFloorNumber());
        dto.setFloorsTotal(apartment.getFloorsTotal());
        dto.setArea(apartment.getArea());

        dto.setTypeOfRent(apartment.getTypeOfRent());
        dto.setRentalFee(apartment.getRentalFee());
        dto.setFeePeriod(apartment.getFeePeriod());

        dto.setCreated(apartment.getCreated());
        dto.setUpdated(apartment.getUpdated());

        dto.setPhotos(apartment.listPhotosNewestFirst()
                                    .stream()
                                    .map(ApartmentPhotoDTO::from)
                                    .collect(Collectors.toList())
        );

        dto.setPublished(apartment.isPublished());

        dto.setOwner(UserDTO.from(apartment.getOwner()));

        return dto;
    }

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

    public List<String> getAddedTempPhotoGUIDs() {
        return addedTempPhotoGUIDs;
    }

    public void setAddedTempPhotoGUIDs(List<String> addedTempPhotoGUIDs) {
        this.addedTempPhotoGUIDs = new ArrayList<>(addedTempPhotoGUIDs);
    }

    public List<String> getDeletePhotoGUIDs() {
        return deletePhotoGUIDs;
    }

    public void setDeletePhotoGUIDs(List<String> deletePhotoGUIDs) {
        this.deletePhotoGUIDs = new ArrayList<>(deletePhotoGUIDs);
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public InternalApartment toInternal() {
        InternalApartment apartment = new InternalApartment();
        apartment.setId(getId());
        AddressComponentsDTO address = getAddress();
        apartment.setAddressComponents(address != null ? address.toInternal() :  null);
        apartment.setArea(getArea());
        apartment.setRoomCount(getRoomCount());
        apartment.setFloorNumber(getFloorNumber());
        apartment.setFloorsTotal(getFloorsTotal());
        apartment.setDescription(getDescription());
        apartment.setLocation(getLocation() != null ? getLocation().toInternal() : null);
        apartment.setCreated(getCreated());
        apartment.setUpdated(getUpdated());

        apartment.setTypeOfRent(getTypeOfRent());
        apartment.setRentalFee(getRentalFee());
        apartment.setFeePeriod(getFeePeriod());
        apartment.setPublished(isPublished());
        return apartment;
    }

    public ApartmentInfoDelta toApartmentInfoDelta() {
        ApartmentInfoDelta apartment = new ApartmentInfoDelta();

        AddressComponentsDTO address = getAddress();
        apartment.setAddressComponents(address != null ? address.toInternal() :  null);
        apartment.setArea(getArea());
        apartment.setRoomCount(getRoomCount());
        apartment.setFloorNumber(getFloorNumber());
        apartment.setFloorsTotal(getFloorsTotal());

        apartment.setLocation(getLocation() != null ? getLocation().toInternal() : null);

        return apartment;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }
}
