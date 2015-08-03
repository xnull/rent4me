package bynull.realty.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Created by dionis on 19/01/15.
 */
@Getter
@Setter
public class MetroDTO {
    private Long id;
    private String stationName;
    private GeoPointDTO location;
    private CityDTO city;

    public void setCityOpt(Optional<CityDTO> city){
        city.ifPresent(c -> this.city = c);
    }
}
