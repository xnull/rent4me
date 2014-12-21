package bynull.realty.web.json;

import bynull.realty.data.business.AddressComponents;
import bynull.realty.data.business.FeePeriod;
import bynull.realty.data.business.RentType;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.web.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 22/06/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ApartmentJSON {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("location")
    private GeoPointJSON location;
    @JsonProperty("address")
    private AddressComponentsJSON address;

    @JsonProperty("description")
    private String description;

    @JsonProperty("room_count")
    private Integer roomCount;

    @JsonProperty("floor_number")
    private Integer floorNumber;

    @JsonProperty("floors_total")
    private Integer floorsTotal;

    @JsonProperty("area")
    private BigDecimal area;

    @JsonProperty("type_of_rent")
    private RentType typeOfRent;
    @JsonProperty("rental_fee")
    private BigDecimal rentalFee;
    @JsonProperty("fee_period")
    private FeePeriod feePeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
    @JsonProperty("created")
    private Date created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
    @JsonProperty("updated")
    private Date updated;

    @JsonProperty("published")
    private boolean published;

    @JsonProperty("owner")
    private UserJSON owner;

    @JsonProperty("photos")
    private List<ApartmentPhotoJSON> photos = Collections.emptyList();
    @JsonProperty("added_photos_guids")
    private List<String> addedTempPhotoGUIDs = Collections.emptyList();
    @JsonProperty("deleted_photos_guids")
    private List<String> deletePhotoGUIDs = Collections.emptyList();

    public static ApartmentJSON from(ApartmentDTO apartment) {
        if (apartment == null) return null;
        ApartmentJSON json = new ApartmentJSON();
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

        json.setPhotos(apartment.getPhotos()
                                    .stream()
                                    .map(ApartmentPhotoJSON::from)
                                    .collect(Collectors.toList())
        );

        json.setPublished(apartment.isPublished());

        json.setOwner(UserJSON.from(apartment.getOwner()));

        return json;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeoPointJSON getLocation() {
        return location;
    }

    public void setLocation(GeoPointJSON location) {
        this.location = location;
    }

    public AddressComponentsJSON getAddress() {
        return address;
    }

    public void setAddress(AddressComponentsJSON address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Integer getFloorsTotal() {
        return floorsTotal;
    }

    public void setFloorsTotal(Integer floorsTotal) {
        this.floorsTotal = floorsTotal;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public RentType getTypeOfRent() {
        return typeOfRent;
    }

    public void setTypeOfRent(RentType typeOfRent) {
        this.typeOfRent = typeOfRent;
    }

    public BigDecimal getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(BigDecimal rentalFee) {
        this.rentalFee = rentalFee;
    }

    public FeePeriod getFeePeriod() {
        return feePeriod;
    }

    public void setFeePeriod(FeePeriod feePeriod) {
        this.feePeriod = feePeriod;
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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public List<ApartmentPhotoJSON> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ApartmentPhotoJSON> photos) {
        this.photos = photos;
    }

    public List<String> getAddedTempPhotoGUIDs() {
        return addedTempPhotoGUIDs;
    }

    public void setAddedTempPhotoGUIDs(List<String> addedTempPhotoGUIDs) {
        this.addedTempPhotoGUIDs = addedTempPhotoGUIDs;
    }

    public List<String> getDeletePhotoGUIDs() {
        return deletePhotoGUIDs;
    }

    public void setDeletePhotoGUIDs(List<String> deletePhotoGUIDs) {
        this.deletePhotoGUIDs = deletePhotoGUIDs;
    }

    public UserJSON getOwner() {
        return owner;
    }

    public void setOwner(UserJSON owner) {
        this.owner = owner;
    }

    public ApartmentDTO toDTO() {
        ApartmentDTO dto = new ApartmentDTO();
        dto.setId(getId());
        AddressComponentsJSON address = getAddress();
        dto.setAddress(address != null ? address.toDTO() : null);
        dto.setCreated(getCreated());
        dto.setUpdated(getUpdated());
        dto.setLocation(getLocation() != null ? getLocation().toDTO(): null);
        dto.setArea(getArea());
        dto.setRoomCount(getRoomCount());
        dto.setFloorNumber(getFloorNumber());
        dto.setFloorsTotal(getFloorsTotal());
        dto.setDescription(getDescription());

        dto.setTypeOfRent(getTypeOfRent());
        dto.setRentalFee(getRentalFee());
        dto.setFeePeriod(getFeePeriod());

        dto.setAddedTempPhotoGUIDs(getAddedTempPhotoGUIDs());
        dto.setDeletePhotoGUIDs(getDeletePhotoGUIDs());

        dto.setPublished(isPublished());

        return dto;
    }
}
