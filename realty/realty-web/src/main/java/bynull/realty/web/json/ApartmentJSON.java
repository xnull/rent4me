package bynull.realty.web.json;

import bynull.realty.dto.ApartmentDTO;
import bynull.realty.web.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

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
    @JsonProperty("city")
    private String city;
    @JsonProperty("address")
    private String address;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
    @JsonProperty("created")
    private Date created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
    @JsonProperty("updated")
    private Date updated;

    public static ApartmentJSON from(ApartmentDTO apartment) {
        if (apartment == null) return null;
        ApartmentJSON json = new ApartmentJSON();
        json.setId(apartment.getId());
        json.setLocation(GeoPointJSON.from(apartment.getLocation()));
        json.setCity(apartment.getCity());
        json.setAddress(apartment.getAddress());
        json.setCreated(apartment.getCreated());
        json.setUpdated(apartment.getUpdated());
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

    public ApartmentDTO toDTO() {
        ApartmentDTO dto = new ApartmentDTO();
        dto.setId(getId());
        dto.setAddress(getAddress());
        dto.setCity(getCity());
        dto.setCreated(getCreated());
        dto.setUpdated(getUpdated());
        dto.setLocation(getLocation() != null ? getLocation().toDTO(): null);
        return dto;
    }
}
