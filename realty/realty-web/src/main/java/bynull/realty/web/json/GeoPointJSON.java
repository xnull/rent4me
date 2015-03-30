package bynull.realty.web.json;

import bynull.realty.dto.GeoPointDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dionis on 22/06/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Getter
@Setter
public class GeoPointJSON {
    public static GeoPointJSON from(GeoPointDTO dto) {
        if (dto == null) return null;
        GeoPointJSON json = new GeoPointJSON();
        json.setLatitude(dto.getLatitude());
        json.setLongitude(dto.getLongitude());
        return json;
    }

    @JsonProperty("latitude")
    private double latitude;
    @JsonProperty("longitude")
    private double longitude;

    public static GeoPointDTO toDTO(GeoPointJSON it) {
        if (it == null) {
            return null;
        }
        GeoPointDTO dto = new GeoPointDTO();
        dto.setLatitude(it.getLatitude());
        dto.setLongitude(it.getLongitude());
        return dto;
    }
}
