package bynull.realty.dto;

import bynull.realty.data.business.Contact;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * Created by dionis on 3/5/15.
 */
@Getter
@Setter
public class ContactDTO {
    private Long id;
    private Contact.Type type;

    //phone specific
    private PhoneNumberDTO phoneNumber;

    @JsonIgnore
    public Optional<PhoneNumberDTO> getPhoneNumberOpt(){
        return Optional.ofNullable(phoneNumber);
    }
}
