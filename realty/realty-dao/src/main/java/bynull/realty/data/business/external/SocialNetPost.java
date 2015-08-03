package bynull.realty.data.business.external;

import bynull.realty.data.business.PhoneNumber;
import bynull.realty.data.business.metro.MetroEntity;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

/**
 * Created by dionis on 04/02/15.
 */
@Immutable
@Entity
@Table(name = "social_net_posts_vw")
@Getter
public class SocialNetPost {
    @EmbeddedId
    private SocialNetPostId id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "imported_dt")
    private Date imported;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "message")
    private String message;

    @Column(name = "picture")
    private String picture;
    @Column(name = "link")
    private String link;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ext_created_dt")
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ext_updated_dt")
    private Date updated;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_network", insertable = false, updatable = false)
    private SocialNetwork socialNetwork;

    @JoinTable(
            name = "posts_to_metros_vw",
            joinColumns = {
                    @JoinColumn(name = "post_id"),
                    @JoinColumn(name = "social_network"),
            },
            inverseJoinColumns = @JoinColumn(name = "metro_station_id")
    )
    @OneToMany
    private Set<MetroEntity> metros;

    @Column(name = "rental_fee")
    private BigDecimal rentalFee;

    @Column(name = "room_count")
    private Integer roomCount;

    @Embedded
    private PhoneNumber phoneNumber;

    public Optional<PhoneNumber> getPhoneNumberOpt(){
        return Optional.ofNullable(phoneNumber);
    }
}
