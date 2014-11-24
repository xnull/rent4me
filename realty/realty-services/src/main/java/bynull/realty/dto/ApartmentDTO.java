package bynull.realty.dto;

import bynull.realty.data.business.AddressComponents;
import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.FeePeriod;
import bynull.realty.data.business.RentType;

import java.math.BigDecimal;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 22/06/14.
 */
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

    private Date created;
    private Date updated;

    public static ApartmentDTO from(Apartment apartment) {
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
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GeoPointDTO getLocation() {
        return location;
    }

    public void setLocation(GeoPointDTO location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public AddressComponentsDTO getAddress() {
        return address;
    }

    public void setAddress(AddressComponentsDTO address) {
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

    public Apartment toInternal() {
        Apartment apartment = new Apartment();
        apartment.setId(getId());
//        apartment.setCity(getCity());
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
        return apartment;
    }
}
