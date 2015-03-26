package name.dargiri.web.form;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.FeePeriod;
import bynull.realty.data.business.RentType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by dionis on 3/25/15.
 */
@Getter
@Setter
public class ApartmentForm {
    private Long id;
    private GeoPointForm location;
    private AddressComponentsForm address;
    private String description;
    private Integer roomCount;
    private Integer floorNumber;
    private Integer floorsTotal;
    private BigDecimal area;
    private RentType typeOfRent;
    private BigDecimal rentalFee;
    private FeePeriod feePeriod;
    private Date created;
    private Date updated;
    private boolean published;
    private Apartment.Target target;
    private List<? extends MetroForm> metros;
    private Apartment.DataSource dataSource;

    //internal specific
    /*
    //TODO: set later
    private UserJSON owner;

    private List<ApartmentPhotoJSON> photos;
    private List<String> addedTempPhotoGUIDs;
    private List<String> deletePhotoGUIDs;

    //socialnet specific
    private List<ApartmentExternalPhotoJSON> imageUrls;

    private List<? extends ContactJSON> contacts;
    */

    private String externalLink;
    private String externalAuthorLink;
}
