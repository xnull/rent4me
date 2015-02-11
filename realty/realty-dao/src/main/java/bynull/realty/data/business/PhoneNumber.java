package bynull.realty.data.business;

import bynull.realty.common.PhoneUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by dionis on 2/11/15.
 */
@Embeddable
@Getter
@Setter
public class PhoneNumber implements Serializable{
    @Column(name = "phone_number")
    private String rawNumber;
    @Column(name = "national_formatted_number")
    private String nationalFormattedNumber;

    public static PhoneNumber from(PhoneUtil.Phone phone) {
        if (phone == null) {
            return null;
        } else {
            PhoneNumber number = new PhoneNumber();
            number.setNationalFormattedNumber(phone.national);
            number.setRawNumber(phone.raw);
            return number;
        }
    }
}
