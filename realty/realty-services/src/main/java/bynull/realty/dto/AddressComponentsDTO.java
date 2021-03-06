package bynull.realty.dto;

import bynull.realty.data.business.AddressComponents;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dionis on 23/11/14.
 */
@Getter
@Setter
public class AddressComponentsDTO {
    private String formattedAddress;
    private String streetAddress;
    private String district;
    private String city;
    private String county;
    private String country;
    private String countryCode;
    private String zipCode;

    public static AddressComponentsDTO from(AddressComponents model) {
        if (model == null) return null;
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

    public static AddressComponents toInternal(AddressComponentsDTO instance) {
        if (instance == null) {
            return null;
        }
        AddressComponents model = new AddressComponents();
        model.setFormattedAddress(instance.getFormattedAddress());

        model.setStreetAddress(instance.getStreetAddress());
        model.setDistrict(instance.getDistrict());
        model.setCity(instance.getCity());
        model.setCounty(instance.getCounty());
        model.setCountry(instance.getCountry());
        model.setCountryCode(instance.getCountryCode());
        model.setZipCode(instance.getZipCode());

        return model;
    }
}
