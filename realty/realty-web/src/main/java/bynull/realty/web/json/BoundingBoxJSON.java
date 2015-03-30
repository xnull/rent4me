package bynull.realty.web.json;

import bynull.realty.dto.BoundingBoxDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 3/30/15.
 */
@Getter
@Setter
public class BoundingBoxJSON {
    @JsonProperty("low")
    private GeoPointJSON low;
    @JsonProperty("high")
    private GeoPointJSON high;
    @JsonProperty("center_point")
    private GeoPointJSON centerPoint;

    public static BoundingBoxJSON from(BoundingBoxDTO boundingBox) {
        if (boundingBox == null) {
            return null;
        }
        BoundingBoxJSON dto = new BoundingBoxJSON();
        dto.setHigh(GeoPointJSON.from(boundingBox.getHigh()));
        dto.setLow(GeoPointJSON.from(boundingBox.getLow()));
        dto.setCenterPoint(GeoPointJSON.from(boundingBox.getCenterPoint()));
        return dto;
    }

    public static BoundingBoxDTO toDTO(BoundingBoxJSON it) {
        if (it == null) {
            return null;
        }
        BoundingBoxDTO dto = new BoundingBoxDTO();
        dto.setHigh(GeoPointJSON.toDTO(it.getHigh()));
        dto.setLow(GeoPointJSON.toDTO(it.getLow()));
        return dto;
    }
}
