package bynull.realty.dto.vk;

import bynull.realty.dto.CityDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Created by dionis on 28/01/15.
 */
@Getter
@Setter
public class VkontaktePageDTO {
    private Long id;
    private String externalId;
    private String link;
    private boolean enabled;
    private CityDTO city;

    @JsonIgnore
    public Optional<CityDTO> getCityOpt(){
        return Optional.ofNullable(city);
    }
}
