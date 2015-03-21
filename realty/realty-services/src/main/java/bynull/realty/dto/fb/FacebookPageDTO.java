package bynull.realty.dto.fb;

import bynull.realty.dto.CityDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 18/01/15.
 */
@Getter
@Setter
public class FacebookPageDTO {
    private Long id;
    private String externalId;
    private String link;
    private boolean enabled;
    private CityDTO city;
}
