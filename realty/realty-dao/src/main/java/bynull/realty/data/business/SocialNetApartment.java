package bynull.realty.data.business;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author dionis on 3/2/15.
 */
@MappedSuperclass
public abstract class SocialNetApartment extends Apartment {
    @NotNull
    @Column(name = "external_id")
    private String externalId;

    /**
     * Link to external profile
     */
    @Column(name = "ext_link")
    private String link;

    @Column(name = "ext_author_id_link")
    private String extAuthorLink;

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "apartment")
    private Set<ApartmentExternalPhoto> externalPhotos;

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
            name = "apartments_contacts",
            joinColumns = @JoinColumn(name = "apartment_id"),
            inverseJoinColumns = @JoinColumn(name = "contact_id")
    )
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Contact> contacts;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getExtAuthorLink() {
        return extAuthorLink;
    }

    public void setExtAuthorLink(String extAuthorLink) {
        this.extAuthorLink = extAuthorLink;
    }

    public Set<ApartmentExternalPhoto> getExternalPhotos() {
        return externalPhotos;
    }

    public void setExternalPhotos(Set<ApartmentExternalPhoto> externalPhotos) {
        this.externalPhotos = externalPhotos;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }
}
