package bynull.realty.data.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by dionis on 3/21/15.
 */
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
}
