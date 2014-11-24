package bynull.realty.dto;

import bynull.realty.data.business.AddressComponents;

import javax.persistence.Column;

/**
 * @author dionis on 23/11/14.
 */
public class AddressComponentsDTO {
    private String formattedAddress;
    private String streetAddress;
    private String district;
    private String city;
    private String county;
    private String country;
    private String countryCode;
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

    public static AddressComponentsDTO from(AddressComponents model) {
        if(model == null) return null;
        AddressComponentsDTO dto = new AddressComponentsDTO();
        dto.setFormattedAddress(model.getFormattedAddress());

        dto.setStreetAddress(model.getStreetAddress());
        dto.setDistrict(model.getDistrict());
        dto.setCity(model.getCity());
        dto.setCounty(model.getCounty());
        dto.setCountry(model.getCountry());
        dto.setCountryCode(model.getCountryCode());
        dto.setZipCode(model.getZipCode());
        return dto;
    }

    public AddressComponents toInternal() {
        AddressComponents model = new AddressComponents();
        model.setFormattedAddress(getFormattedAddress());

        model.setStreetAddress(getStreetAddress());
        model.setDistrict(getDistrict());
        model.setCity(getCity());
        model.setCounty(getCounty());
        model.setCountry(getCountry());
        model.setCountryCode(getCountryCode());
        model.setZipCode(getZipCode());

        return model;
    }
}
