package bynull.realty.jdbc;

import bynull.realty.data.common.GeoPoint;
import org.postgis.PGgeometry;
import org.postgis.Point;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author dionis on 22/06/14.
 */
public class GeoPointColumnMapper {
    public static final GeoPointColumnMapper INSTANCE = new GeoPointColumnMapper();
    private GeoPointColumnMapper(){

    }

    public GeoPoint mapGeoPoint(ResultSet rs, String columnName) throws SQLException {
        PGgeometry lastLocation = (PGgeometry) rs.getObject(columnName);
        if (lastLocation != null) {
            GeoPoint geoPoint = new GeoPoint();
            geoPoint.setLongitude(((Point) lastLocation.getGeometry()).getX());
            geoPoint.setLatitude(((Point) lastLocation.getGeometry()).getY());
            return geoPoint;
        } else {
            return null;
        }
    }
}
