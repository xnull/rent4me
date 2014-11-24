package bynull.realty.data.business;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author dionis on 24/06/14.
 */
@Embeddable
public class AddressComponents implements Serializable {
    @Column(name = "formatted_address")
    private String formattedAddress;
    @Column(name = "street_address")
    private String streetAddress;
    @Column(name = "district")
    private String district;
    @Column(name = "city")
    private String city;
    @Column(name = "county")
    private String county;
    @Column(name = "country")
    private String country;
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "zip_code")
    private String zipCode;

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

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }
}
