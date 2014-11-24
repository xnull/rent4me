package bynull.realty.web.json;

import bynull.realty.dto.AddressComponentsDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author dionis on 23/11/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
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

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

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
