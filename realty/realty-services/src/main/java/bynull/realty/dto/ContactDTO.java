package bynull.realty.dto;

import bynull.realty.data.business.Contact;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Created by dionis on 3/5/15.
 */
@Getter
@Setter
public class ContactDTO {
    private Long id;
    private Contact.Type type;
    private PhoneNumberDTO phoneNumber;
}
