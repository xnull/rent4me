package bynull.realty.data.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.postgis.Point;

import java.io.Serializable;

/**
 * @author dionis on 22/06/14.
 */
@NoArgsConstructor
@AllArgsConstructor
@Wither
public class GeoPoint implements Serializable {

    public static GeoPoint from(Point point) {
        if (point == null) {
            return null;
        }

        GeoPoint geoPoint = new GeoPoint();
        geoPoint.setLongitude(point.getX());
        geoPoint.setLatitude(point.getY());
        return geoPoint;
    }

    public Point toPoint() {
        Point point = new Point();
        point.setX(getLongitude());
        point.setY(getLatitude());
        point.setSrid(4326);
        return point;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoPoint)) return false;

        GeoPoint geoPoint = (GeoPoint) o;

        if (Double.compare(geoPoint.getLatitude(), getLatitude()) != 0) return false;
        if (Double.compare(geoPoint.getLongitude(), getLongitude()) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getLatitude());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLongitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "GeoPoint{" +
                "latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                '}';
    }

    public GeoPoint copy() {
        return new GeoPoint().withLatitude(latitude).withLongitude(longitude);
    }
}
