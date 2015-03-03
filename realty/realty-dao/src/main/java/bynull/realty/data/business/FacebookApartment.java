package bynull.realty.data.business;

import bynull.realty.data.common.GeoPoint;
import bynull.realty.hibernate.validation.annotations.LessThanOrEqual;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by null on 21.06.14.
 */
@Entity
@DiscriminatorValue(Apartment.DbValue.FACEBOOK_STRING_DB_VALUE)
public class FacebookApartment extends SocialNetApartment {

}
