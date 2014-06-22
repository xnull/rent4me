package bynull.realty.web.json;

import bynull.realty.dto.GeoPointDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author dionis on 22/06/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GeoPointJSON {
    public static GeoPointJSON from(GeoPointDTO dto) {
        if(dto == null) return null;
        GeoPointJSON json = new GeoPointJSON();
        json.setLatitude(dto.getLatitude());
        json.setLongitude(dto.getLongitude());
        return json;
    }

    @JsonProperty("latitude")
    private double latitude;
    @JsonProperty("longitude")
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public GeoPointDTO toDTO() {
        GeoPointDTO dto = new GeoPointDTO();
        dto.setLatitude(getLatitude());
        dto.setLongitude(getLongitude());
        return dto;
    }
}
