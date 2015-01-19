package bynull.realty.data.business.external.facebook;

import bynull.realty.data.business.metro.MetroEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 02/01/15.
 */
@Entity
@Table(name = "facebook_scraped_posts")
public class FacebookScrapedPost {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "fb_scraped_post_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "fb_scraped_post_id_generator", sequenceName = "fb_scr_post_id_seq", allocationSize = 1)
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
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private FacebookPostType type;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ext_created_dt")
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ext_updated_dt")
    private Date updated;

    @NotNull
    @JoinColumn(name = "fb_pg2_scrap_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FacebookPageToScrap facebookPageToScrap;

    @JoinColumn(name = "metro_station_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MetroEntity metro;

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

    public FacebookPostType getType() {
        return type;
    }

    public void setType(FacebookPostType type) {
        this.type = type;
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

    public FacebookPageToScrap getFacebookPageToScrap() {
        return facebookPageToScrap;
    }

    public void setFacebookPageToScrap(FacebookPageToScrap facebookPageToScrap) {
        this.facebookPageToScrap = facebookPageToScrap;
    }

    public MetroEntity getMetro() {
        return metro;
    }

    public void setMetro(MetroEntity metro) {
        this.metro = metro;
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
        if (!(o instanceof FacebookScrapedPost)) return false;

        FacebookScrapedPost that = (FacebookScrapedPost) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
