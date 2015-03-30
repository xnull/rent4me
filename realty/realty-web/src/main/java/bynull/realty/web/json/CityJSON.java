package bynull.realty.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 3/30/15.
 */
@Getter
@Setter
public class CityJSON {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("area")
    BoundingBoxJSON area;
}
