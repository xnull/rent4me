package bynull.realty.web.json;

import bynull.realty.data.business.FeePeriod;
import bynull.realty.data.business.RentType;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dao.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
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
