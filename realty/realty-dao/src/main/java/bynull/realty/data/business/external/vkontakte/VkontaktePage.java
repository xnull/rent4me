package bynull.realty.data.business.external.vkontakte;

import javax.persistence.*;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 28/01/15.
 */
@Entity
@Table(name = "vk_page")
public class VkontaktePage {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "vk_page_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "vk_page_id_generator", sequenceName = "vk_page_id_seq", allocationSize = 1)
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

    public VkontaktePage() {

    }

    public VkontaktePage(long id) {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
        if (!(o instanceof VkontaktePage)) return false;

        VkontaktePage that = (VkontaktePage) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
