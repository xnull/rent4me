package bynull.realty.data.business;

import bynull.realty.data.business.User;

import javax.persistence.*;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 4/11/15.
 */
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "feedback_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "feedback_id_generator", sequenceName = "feedback_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "text")
    private String text;
    @JoinColumn(name = "creator_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt")
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt")
    private Date updated;

    public Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreated() {
        return copy(created);
    }

    void setCreated(Date created) {
        this.created = copy(created);
    }

    public Date getUpdated() {
        return copy(updated);
    }

    void setUpdated(Date updated) {
        this.updated = copy(updated);
    }

    @PrePersist
    void prePersist() {
        Date now = new Date();
        setCreated(now);
        setUpdated(now);
    }

    @PreUpdate
    void preUpdate() {
        Date now = new Date();
        setUpdated(now);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;

        Feedback feedback = (Feedback) o;

        if (getId() != null ? !getId().equals(feedback.getId()) : feedback.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
