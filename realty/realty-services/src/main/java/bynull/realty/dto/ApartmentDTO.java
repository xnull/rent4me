package bynull.realty.dto;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.common.GeoPoint;

import java.util.Date;

/**
 * @author dionis on 22/06/14.
 */
public class ApartmentDTO {
    public static ApartmentDTO from(Apartment apartment) {
        if(apartment == null) return null;
        ApartmentDTO dto = new ApartmentDTO();
        dto.setId(apartment.getId());
        dto.setLocation(GeoPointDTO.from(apartment.getLocation()));
        dto.setCity(apartment.getCity());
        dto.setAddress(apartment.getAddress());
        dto.setCreated(apartment.getCreated());
        dto.setUpdated(apartment.getUpdated());
        return dto;
    }

    private Long id;
    private GeoPointDTO location;
    private String city;
    private String address;
    private Date created;
    private Date updated;

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
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Apartment toInternal() {
        Apartment apartment = new Apartment();
        apartment.setId(getId());
        apartment.setCity(getCity());
        apartment.setAddress(getAddress());
        apartment.setLocation(getLocation() != null ? getLocation().toInternal() : null);
        apartment.setCreated(getCreated());
        apartment.setUpdated(getUpdated());
        return apartment;
    }
}
