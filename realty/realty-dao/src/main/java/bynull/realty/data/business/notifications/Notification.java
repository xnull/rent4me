package bynull.realty.data.business.notifications;

import bynull.realty.data.business.User;

import javax.persistence.*;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 5/2/15.
 */
@Entity
@Table(name = "notifications")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Notification {

    public static class Types {
        public static final String NEW_MESSAGE_STRING_VALUE = "1";
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "notification_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "notification_id_generator", sequenceName = "notification_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "resolved")
    private boolean resolved;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt")
    private Date created;

    @Column(name = "updated_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
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
        if (!(o instanceof Notification)) return false;

        Notification that = (Notification) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    public abstract Type getType();

    public static enum Type {
        NEW_MESSAGE(Types.NEW_MESSAGE_STRING_VALUE);

        private final int dbValue;

        Type(String dbValue) {
            this.dbValue = Integer.valueOf(dbValue);
        }

        public int getDbValue() {
            return dbValue;
        }
    }
}
