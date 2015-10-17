package bynull.realty.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by dionis on 3/21/15.
 */
@ToString
@Getter
@Setter
public class CityDTO {
    private Long id;
    private String name;
    BoundingBoxDTO area;
    CountryDTO country;
}
