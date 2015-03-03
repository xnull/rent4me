package bynull.realty.data.business;

import javax.persistence.*;
import java.util.Date;

/**
 * @author dionis on 3/2/15.
 */
@Entity
@Table(name = "apartment_external_photos")
public class ApartmentExternalPhoto {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "ext_photos_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ext_photos_id_generator", sequenceName = "ext_apt_photo_id_seq", allocationSize = 1)
    private Long id;

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
}
