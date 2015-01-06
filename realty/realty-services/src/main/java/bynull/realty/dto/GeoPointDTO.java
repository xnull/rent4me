package bynull.realty.dto;

import bynull.realty.data.common.GeoPoint;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dionis on 22/06/14.
 */
@Getter
@Setter
public class GeoPointDTO {
    public static GeoPointDTO from(GeoPoint geoPoint) {
        if(geoPoint == null) return null;

        GeoPointDTO geoPointDTO = new GeoPointDTO();
        geoPointDTO.setLatitude(geoPoint.getLatitude());
        geoPointDTO.setLongitude(geoPoint.getLongitude());
        return geoPointDTO;
    }

    private double latitude;
    private double longitude;

    public GeoPoint toInternal() {
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setLatitude(getLatitude());
        geoPoint.setLongitude(getLongitude());
        return geoPoint;
    }
}
