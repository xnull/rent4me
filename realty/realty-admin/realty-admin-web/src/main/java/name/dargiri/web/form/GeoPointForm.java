package name.dargiri.web.form;

import bynull.realty.dto.GeoPointDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 3/21/15.
 */
@Getter
@Setter
public class GeoPointForm {
    public static GeoPointForm from(GeoPointDTO dto) {
        if (dto == null) return null;
        GeoPointForm json = new GeoPointForm();
        json.setLatitude(dto.getLatitude());
        json.setLongitude(dto.getLongitude());
        return json;
    }

    private double latitude;
    private double longitude;

    public static GeoPointDTO toDTO(GeoPointForm form) {
        if (form == null) {
            return null;
        }
        GeoPointDTO dto = new GeoPointDTO();
        dto.setLatitude(form.getLatitude());
        dto.setLongitude(form.getLongitude());
        return dto;
    }
}
