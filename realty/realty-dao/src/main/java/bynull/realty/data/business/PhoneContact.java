package bynull.realty.data.business;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Optional;

/**
 * Created by dionis on 3/5/15.
 */
@Entity
@DiscriminatorValue(Contact.DbValue.PHONE_DB_VALUE)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PhoneContact extends Contact {
    @Embedded
    private PhoneNumber phoneNumber;

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<PhoneNumber> getPhoneNumberOpt() {
        return Optional.ofNullable(phoneNumber);
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Type getType() {
        return Type.PHONE;
    }
}
