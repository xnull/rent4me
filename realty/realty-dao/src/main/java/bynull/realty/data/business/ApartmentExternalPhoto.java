package bynull.realty.data.business;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 3/2/15.
 */
@Entity
@Table(name = "apartment_external_photos")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApartmentExternalPhoto implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "ext_photos_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ext_photos_id_generator", sequenceName = "ext_apt_photo_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "preview_image_url")
    private String previewUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt")
    private Date updated;

    @JoinColumn(name = "apartment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Apartment apartment;

    public Long getId() {
        return id;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreated() {
        return copy(created);
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return copy(updated);
    }


    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @PrePersist
    void prePersist() {
        Date now = new Date();
        created = now;
        updated = now;
    }

    @PreUpdate
    void preUpdate() {
        updated = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApartmentExternalPhoto)) return false;

        ApartmentExternalPhoto that = (ApartmentExternalPhoto) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
