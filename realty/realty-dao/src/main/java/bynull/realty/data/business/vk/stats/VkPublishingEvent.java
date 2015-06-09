package bynull.realty.data.business.vk.stats;

import bynull.realty.data.business.Apartment;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author dionis
 *         07/06/15.
 */
@Entity
@Table(name = "vk_publishing_events")
public class VkPublishingEvent {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "target_group")
    private String targetGroup;
    //which token was used for publishing - this info is useful because we have restrictions on how much items user might post
    @Column(name = "used_token")
    private String usedToken;
    @Column(name = "text_published")
    private String textPublished;
    @Column(name = "data_source")
    @Enumerated(EnumType.STRING)
    private Apartment.DataSource dataSource;
    @Column(name = "created_dt")
    private Date created;

    @PrePersist
    public void prePersist() {
        setId(UUID.randomUUID());
        created = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUsedToken() {
        return usedToken;
    }

    public void setUsedToken(String usedToken) {
        this.usedToken = usedToken;
    }

    public String getTextPublished() {
        return textPublished;
    }

    public void setTextPublished(String textPublished) {
        this.textPublished = textPublished;
    }

    public Apartment.DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(Apartment.DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VkPublishingEvent)) return false;

        VkPublishingEvent that = (VkPublishingEvent) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
