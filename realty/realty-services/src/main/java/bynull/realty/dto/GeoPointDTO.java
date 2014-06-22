package bynull.realty.dto;

import bynull.realty.data.common.GeoPoint;

/**
 * @author dionis on 22/06/14.
 */
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

    public GeoPoint toInternal() {
        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setLatitude(getLatitude());
        geoPoint.setLongitude(getLongitude());
        return geoPoint;
    }
}
