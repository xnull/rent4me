package bynull.realty.data.business.external.facebook;

import bynull.realty.data.common.CityEntity;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 02/01/15.
 */
@ToString
@Entity
@Table(name = "facebook_page_to_scrap")
public class FacebookPageToScrap {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "fb_pg2scrap_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "fb_pg2scrap_id_generator", sequenceName = "fb_pg2scrap_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "external_id")
    private String externalId;

    @Column(name = "link")
    private String link;

    @Column(name = "enabled")
    private boolean enabled;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt")
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt")
    private Date updated;

    @JoinColumn(name = "city_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CityEntity city;

    public FacebookPageToScrap() {

    }

    public FacebookPageToScrap(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    @PrePersist
    void prePersist() {
        Date date = new Date();
        setCreated(date);
        setUpdated(date);
    }

    @PreUpdate
    void preUpdate() {
        Date date = new Date();
        setUpdated(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacebookPageToScrap)) return false;

        FacebookPageToScrap that = (FacebookPageToScrap) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
