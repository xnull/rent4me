package bynull.realty.data.business;

import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
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
    @NotNull
    @JoinColumn(name = "fb_page_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FacebookPageToScrap facebookPage;

    public FacebookPageToScrap getFacebookPage() {
        return facebookPage;
    }

    public void setFacebookPage(FacebookPageToScrap facebookPage) {
        this.facebookPage = facebookPage;
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.FACEBOOK;
    }
}
