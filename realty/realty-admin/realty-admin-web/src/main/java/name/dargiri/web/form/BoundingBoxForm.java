package name.dargiri.web.form;

import bynull.realty.data.common.BoundingBox;
import bynull.realty.dto.BoundingBoxDTO;
import bynull.realty.dto.GeoPointDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 3/21/15.
 */
@Getter
@Setter
public class BoundingBoxForm {
    private GeoPointForm low;
    private GeoPointForm high;

    public static BoundingBoxForm from(BoundingBoxDTO boundingBox) {
        if (boundingBox == null) {
            return null;
        }
        BoundingBoxForm dto = new BoundingBoxForm();
        dto.setHigh(GeoPointForm.from(boundingBox.getHigh()));
        dto.setLow(GeoPointForm.from(boundingBox.getLow()));
        return dto;
    }

    public static BoundingBoxDTO toDTO(BoundingBoxForm it) {
        if (it == null) {
            return null;
        }
        BoundingBoxDTO dto = new BoundingBoxDTO();
        dto.setHigh(GeoPointForm.toDTO(it.getHigh()));
        dto.setLow(GeoPointForm.toDTO(it.getLow()));
        return dto;
    }
}
