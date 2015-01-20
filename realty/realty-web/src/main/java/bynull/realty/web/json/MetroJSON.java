package bynull.realty.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 20/01/15.
 */
@Getter
@Setter
public class MetroJSON {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("station_name")
    private String stationName;
}
