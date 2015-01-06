package bynull.realty.web.json;

import bynull.realty.dto.AddressComponentsDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dionis on 23/11/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
@Getter
@Setter
public class AddressComponentsJSON {

    @JsonProperty("formatted_address")
    private String formattedAddress;
    @JsonProperty("street_address")
    private String streetAddress;
    @JsonProperty("district")
    private String district;
    @JsonProperty("city")
    private String city;
    @JsonProperty("county")
    private String county;
    @JsonProperty("country")
    private String country;
    @JsonProperty("country_code")
    private String countryCode;
    @JsonProperty("zip_code")
    private String zipCode;

    public static AddressComponentsJSON from(AddressComponentsDTO dto) {
        if(dto == null) return null;
        AddressComponentsJSON json = new AddressComponentsJSON();
        json.setFormattedAddress(dto.getFormattedAddress());

        json.setStreetAddress(dto.getStreetAddress());
        json.setDistrict(dto.getDistrict());
        json.setCity(dto.getCity());
        json.setCounty(dto.getCounty());
        json.setCountry(dto.getCountry());
        json.setCountryCode(dto.getCountryCode());
        json.setZipCode(dto.getZipCode());
        return json;
    }

    public AddressComponentsDTO toDTO() {
        AddressComponentsDTO dto = new AddressComponentsDTO();
        dto.setFormattedAddress(getFormattedAddress());

        dto.setStreetAddress(getStreetAddress());
        dto.setDistrict(getDistrict());
        dto.setCity(getCity());
        dto.setCounty(getCounty());
        dto.setCountry(getCountry());
        dto.setCountryCode(getCountryCode());
        dto.setZipCode(getZipCode());
        return dto;
    }
}
