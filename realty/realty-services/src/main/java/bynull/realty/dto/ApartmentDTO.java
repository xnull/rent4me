package bynull.realty.dto;

import bynull.realty.data.business.Apartment;

import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 22/06/14.
 */
public class ApartmentDTO {
    private Long id;
    private GeoPointDTO location;
    private String city;
    private String address;
    private Date created;
    private Date updated;

    public static ApartmentDTO from(Apartment apartment) {
        if (apartment == null) return null;
        ApartmentDTO dto = new ApartmentDTO();
        dto.setId(apartment.getId());
        dto.setLocation(GeoPointDTO.from(apartment.getLocation()));
//        dto.setCity(apartment.getCity());
//        dto.setAddress(apartment.getAddress());
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
//        apartment.setAddress(getAddress());
        apartment.setLocation(getLocation() != null ? getLocation().toInternal() : null);
        apartment.setCreated(getCreated());
        apartment.setUpdated(getUpdated());
        return apartment;
    }
}
