package bynull.realty.data.business;

import bynull.realty.data.business.external.vkontakte.VkontaktePage;
import bynull.realty.data.common.CityEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author dionis on 3/2/15.
 */
@Entity
@DiscriminatorValue(Apartment.DbValue.VKONTAKTE_STRING_DB_VALUE)
public class VkontakteApartment extends SocialNetApartment {
    @NotNull
    @JoinColumn(name = "vk_page_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private VkontaktePage vkontaktePage;

    public VkontaktePage getVkontaktePage() {
        return vkontaktePage;
    }

    public void setVkontaktePage(VkontaktePage vkontaktePage) {
        this.vkontaktePage = vkontaktePage;
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.VKONTAKTE;
    }

    @Override
    public ApartmentType getType() {
        return ApartmentType.VK;
    }

    @Override
    public CityEntity getCity() {
        return vkontaktePage.getCity();
    }
}
