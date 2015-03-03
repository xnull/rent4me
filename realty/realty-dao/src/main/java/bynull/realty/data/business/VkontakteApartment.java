package bynull.realty.data.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author dionis on 3/2/15.
 */
@Entity
@DiscriminatorValue(Apartment.DbValue.VKONTAKTE_STRING_DB_VALUE)
public class VkontakteApartment extends SocialNetApartment {
}
