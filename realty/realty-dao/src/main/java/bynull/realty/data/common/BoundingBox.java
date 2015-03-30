package bynull.realty.data.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Wither;

import java.io.Serializable;

/**
 * Created by dionis on 3/21/15.
 */
@AllArgsConstructor
@NoArgsConstructor
@Wither
@Getter
@Setter
public class BoundingBox implements Serializable {
    private GeoPoint low;
    private GeoPoint high;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoundingBox)) return false;

        BoundingBox that = (BoundingBox) o;

        if (high != null ? !high.equals(that.high) : that.high != null) return false;
        if (low != null ? !low.equals(that.low) : that.low != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = low != null ? low.hashCode() : 0;
        result = 31 * result + (high != null ? high.hashCode() : 0);
        return result;
    }

    public boolean contains(GeoPoint geoPoint) {
        boolean result = true;
        result &= low.getLongitude() <= geoPoint.getLongitude() && geoPoint.getLongitude() <= high.getLongitude();
        result &= low.getLatitude() <= geoPoint.getLatitude() && geoPoint.getLatitude() <= high.getLatitude();
        return result;
    }

    public GeoPoint getCenterPoint() {
        return new GeoPoint().withLatitude( (low.getLatitude()+high.getLatitude())/2.0 ).withLongitude( (low.getLongitude()+high.getLongitude())/2.0 );
    }
}
