package bynull.realty.utils;

import bynull.realty.data.common.GeoPoint;

/**
 * @author dionis on 08/12/14.
 */
public class CoordinateUtils {
    public static final double EARTH_RADIUS_METERS = 6371009.0D;

    /**
     * Expected data in degrees (X ˚)
     *
     * @param latitudeA in degrees
     * @param longitudeA in degrees
     * @param latitudeB in degrees
     * @param longitudeB in degrees
     * @return distance in meters between two points
     */
    public static double calculateDistance(double latitudeA, double longitudeA, double latitudeB, double longitudeB) {
        latitudeA = Math.toRadians(latitudeA);
        latitudeB = Math.toRadians(latitudeB);
        longitudeA = Math.toRadians(longitudeA);
        longitudeB = Math.toRadians(longitudeB);
        // radius in meters
        //geographical distance
        //haversine formula
        return EARTH_RADIUS_METERS *2.0d*Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(delta(latitudeA, latitudeB) / 2), 2.0d)
                                +
                                (
                                        Math.cos(latitudeA) * Math.cos(latitudeB) * Math.pow(Math.sin(delta(longitudeA, longitudeB) / 2), 2.0d)
                                )
                )
        );
    }

    public static double calculateDistance(GeoPoint a, GeoPoint b) {
        if((a==null) || (b==null)){
            return Double.MAX_VALUE;
        }

        return calculateDistance(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
    }

    private static double delta(double a, double b) {
        return Math.abs(Math.max(a, b) - Math.min(a, b));
    }
}
