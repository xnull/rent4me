package bynull.realty.data.business;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * @author dionis on 3/2/15.
 */
@MappedSuperclass
public abstract class SocialNetApartment extends Apartment {
    @OneToMany(mappedBy = "apartment")
    private Set<ApartmentExternalPhoto> externalPhotos;

    public Set<ApartmentExternalPhoto> getExternalPhotos() {
        return externalPhotos;
    }

    public void setExternalPhotos(Set<ApartmentExternalPhoto> externalPhotos) {
        this.externalPhotos = externalPhotos;
    }
}
