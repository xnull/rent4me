package bynull.realty.data.business.external.vkontakte;

import bynull.realty.data.business.PhoneNumber;
import bynull.realty.data.business.external.facebook.FacebookPageToScrap;
import bynull.realty.data.business.external.facebook.FacebookPostType;
import bynull.realty.data.business.metro.MetroEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 02/01/15.
 */
@Entity
@Table(name = "vk_posts")
public class VkontaktePost {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "vk_post_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "vk_post_id_generator", sequenceName = "vk_post_id_seq", allocationSize = 1)
    private Long id;

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

    @NotNull
    @JoinColumn(name = "vk_page_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private VkontaktePage vkontaktePage;

    @Embedded
    private PhoneNumber phoneNumber;

    @JoinTable(
            name = "vk_posts_to_metros",
            joinColumns = @JoinColumn(name = "vk_post_id"),
            inverseJoinColumns = @JoinColumn(name = "metro_station_id")
    )
    @OneToMany
    private Set<MetroEntity> metros = new HashSet<>();

    @Column(name = "rental_fee")
    private BigDecimal rentalFee;

    @Column(name = "room_count")
    private Integer roomCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getImported() {
        return imported;
    }

    public void setImported(Date imported) {
        this.imported = imported;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getCreated() {
        return copy(created);
    }

    public void setCreated(Date created) {
        this.created = copy(created);
    }

    public Date getUpdated() {
        return copy(updated);
    }

    public void setUpdated(Date updated) {
        this.updated = copy(updated);
    }

    public VkontaktePage getVkontaktePage() {
        return vkontaktePage;
    }

    public Optional<VkontaktePage> getVkontaktePageOpt() {
        return Optional.ofNullable(vkontaktePage);
    }

    public void setVkontaktePage(VkontaktePage vkontaktePage) {
        this.vkontaktePage = vkontaktePage;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<MetroEntity> getMetros() {
        return metros;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<PhoneNumber> getPhoneNumberOpt() {
        return Optional.ofNullable(phoneNumber);
    }

    public void setMetros(Set<MetroEntity> metros) {
        this.metros = metros;
    }

    public BigDecimal getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(BigDecimal rentalFee) {
        this.rentalFee = rentalFee;
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    @PrePersist
    void prePersist() {
        Date date = new Date();
        setImported(date);
        if (getCreated() == null) {
            setCreated(date);
        }
        if (getUpdated() == null) {
            setUpdated(date);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VkontaktePost)) return false;

        VkontaktePost that = (VkontaktePost) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
