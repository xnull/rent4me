package bynull.realty.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * Created by dionis on 3/25/15.
 */
@Getter
@Setter
public class ApartmentInfoDeltaDTO {
    private Long id;
    private GeoPointDTO location;
    private AddressComponentsDTO addressComponents;
    private Date created;
    private Date updated;
    private boolean applied;
    private boolean rejected;
    private Integer roomCount;
    private Integer floorNumber;
    private Integer floorsTotal;
    private BigDecimal area;
    private ApartmentDTO apartment;

    @JsonIgnore
    public Optional<ApartmentDTO> getApartmentOpt(){
        return Optional.ofNullable(apartment);
    }
}
