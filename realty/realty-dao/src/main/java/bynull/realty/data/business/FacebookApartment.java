package bynull.realty.data.business;

import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.data.common.CityEntity;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by null on 21.06.14.
 */
@ToString
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

    @Override
    public ApartmentType getType() {
        return ApartmentType.FB;
    }

    @Override
    public CityEntity getCity() {
        return facebookPage.getCity();
    }
}
