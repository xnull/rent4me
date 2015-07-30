package bynull.realty.web.json;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.FeePeriod;
import bynull.realty.data.business.RentType;
import bynull.realty.dao.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author dionis on 22/06/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Getter
@Setter
public class ApartmentJSON {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("location")
    private GeoPointJSON location;
    @JsonProperty("address")
    private AddressComponentsJSON address;

    @JsonProperty("description")
    private String description;

    @JsonProperty("room_count")
    private Integer roomCount;

    @JsonProperty("floor_number")
    private Integer floorNumber;

    @JsonProperty("floors_total")
    private Integer floorsTotal;

    @JsonProperty("area")
    private BigDecimal area;

    @JsonProperty("type_of_rent")
    private RentType typeOfRent;
    @JsonProperty("rental_fee")
    private BigDecimal rentalFee;
    @JsonProperty("fee_period")
    private FeePeriod feePeriod;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
    @JsonProperty("created")
    private Date created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.ISO_DATE_TIME_FORMAT)
    @JsonProperty("updated")
    private Date updated;

    @JsonProperty("published")
    private boolean published;

    @JsonProperty("metros")
    private List<? extends MetroJSON> metros;

    @JsonProperty("data_source")
    private DataSource dataSource;

    //internal specific

    @JsonProperty("owner")
    private UserJSON owner;

    @JsonProperty("photos")
    private List<ApartmentPhotoJSON> photos;
    @JsonProperty("added_photos_guids")
    private List<String> addedTempPhotoGUIDs;
    @JsonProperty("deleted_photos_guids")
    private List<String> deletePhotoGUIDs;

    //socialnet specific
    @JsonProperty("external_images")
    private List<ApartmentExternalPhotoJSON> imageUrls;

    @JsonProperty("contacts")
    private List<? extends ContactJSON> contacts;

    @JsonProperty("external_link")
    private String externalLink;

    @JsonProperty("external_author_link")
    private String externalAuthorLink;

    @JsonProperty("city")
    private String city;

    public enum DataSource {
        INTERNAL, FACEBOOK, VKONTAKTE;

        public static DataSource from(Apartment.DataSource dataSource) {
            Assert.notNull(dataSource);

            switch (dataSource) {
                case FACEBOOK:
                    return FACEBOOK;
                case INTERNAL:
                    return INTERNAL;
                case VKONTAKTE:
                    return VKONTAKTE;
                default:
                    throw new UnsupportedOperationException("Datasource " + dataSource);
            }
        }
    }
}
