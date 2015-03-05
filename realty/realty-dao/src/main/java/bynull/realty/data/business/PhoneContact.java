package bynull.realty.data.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by dionis on 3/5/15.
 */
@Entity
@DiscriminatorValue(Contact.DbValue.PHONE_DB_VALUE)
public class PhoneContact extends Contact {
    @Embedded
    private PhoneNumber phoneNumber;

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
