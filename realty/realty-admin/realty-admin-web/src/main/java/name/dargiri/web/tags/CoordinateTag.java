package name.dargiri.web.tags;

import bynull.realty.dto.GeoPointDTO;
import bynull.realty.util.CoordinateUtils;
import name.dargiri.web.form.GeoPointForm;

/**
 * Created by dionis on 3/26/15.
 */
public class CoordinateTag {
    public static double distanceMeters(GeoPointForm g1, GeoPointForm g2) {
        return CoordinateUtils.calculateDistance(
                GeoPointDTO.toInternal(GeoPointForm.toDTO(g1)),
                GeoPointDTO.toInternal(GeoPointForm.toDTO(g2))
        );
    }

    public static double distanceKMs(GeoPointForm g1, GeoPointForm g2) {
        return distanceMeters(g1, g2) / 1000.0d;
    }

    public static String distanceHumanReadable(GeoPointForm g1, GeoPointForm g2) {
        double meters = distanceMeters(g1, g2);
        if(meters >= 1000.0d) {
            return (meters/1000.0d) + " км";
        } else {
            return meters + " м";
        }
    }
}
