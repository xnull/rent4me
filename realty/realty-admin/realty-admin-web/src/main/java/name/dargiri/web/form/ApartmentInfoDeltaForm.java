package name.dargiri.web.form;

import bynull.realty.dto.AddressComponentsDTO;
import bynull.realty.dto.ApartmentDTO;
import bynull.realty.dto.GeoPointDTO;
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
public class ApartmentInfoDeltaForm {
    private Long id;
    private GeoPointForm location;
    private AddressComponentsForm addressComponents;
    private Date created;
    private Date updated;
    private boolean applied;
    private boolean rejected;
    private Integer roomCount;
    private Integer floorNumber;
    private Integer floorsTotal;
    private BigDecimal area;
    private ApartmentForm apartment;

    public void setApartmentOpt(Optional<ApartmentForm> apt){
        apartment = apt.orElse(null);
    }
}
