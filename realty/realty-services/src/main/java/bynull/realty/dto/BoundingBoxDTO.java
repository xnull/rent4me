package bynull.realty.dto;

import bynull.realty.data.common.BoundingBox;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 3/21/15.
 */
@Getter
@Setter
public class BoundingBoxDTO {
    private GeoPointDTO low;
    private GeoPointDTO high;
    private GeoPointDTO centerPoint;

    public static BoundingBoxDTO from(BoundingBox boundingBox) {
        if (boundingBox == null) {
            return null;
        }
        BoundingBoxDTO dto = new BoundingBoxDTO();
        dto.setHigh(GeoPointDTO.from(boundingBox.getHigh()));
        dto.setLow(GeoPointDTO.from(boundingBox.getLow()));
        dto.setCenterPoint(GeoPointDTO.from(boundingBox.getCenterPoint()));
        return dto;
    }

    public static BoundingBox toInternal(BoundingBoxDTO it) {
        if (it == null) {
            return null;
        }
        BoundingBox dto = new BoundingBox();
        dto.setHigh(GeoPointDTO.toInternal(it.getHigh()));
        dto.setLow(GeoPointDTO.toInternal(it.getLow()));
        return dto;
    }
}
