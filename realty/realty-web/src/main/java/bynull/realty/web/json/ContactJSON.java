package bynull.realty.web.json;

import bynull.realty.data.business.Contact;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dionis on 3/6/15.
 */
@Getter
@Setter
public class ContactJSON {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("type")
    private Type type;

    // phone specific
    @JsonProperty("phone")
    private PhoneNumberJSON phone;


    public static enum Type {
        PHONE;

        public static Type from(Contact.Type type) {
            switch (type) {
                case PHONE:
                    return PHONE;
                default:
                    throw new UnsupportedOperationException("Unsupported type "+type);
            }
        }
    }
}
