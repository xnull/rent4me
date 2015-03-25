package name.dargiri.web.form;

import bynull.realty.dto.AddressComponentsDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 3/25/15.
 */
@Getter
@Setter
public class AddressComponentsForm {
    private String formattedAddress;
    private String streetAddress;
    private String district;
    private String city;
    private String county;
    private String country;
    private String countryCode;
    private String zipCode;

    public static AddressComponentsForm from(AddressComponentsDTO model) {
        if(model == null) return null;
        AddressComponentsForm dto = new AddressComponentsForm();
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

    public static AddressComponentsDTO toDTO(AddressComponentsForm instance) {
        if (instance == null) {
            return null;
        }
        AddressComponentsDTO model = new AddressComponentsDTO();
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
