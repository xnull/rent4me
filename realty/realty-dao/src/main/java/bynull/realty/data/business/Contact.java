package bynull.realty.data.business;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 3/5/15.
 */
@Entity
@Table(name = "contacts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Contact {

    public static class DbValue {
        private DbValue() {
        }

        public static final String PHONE_DB_VALUE = "PHONE";
    }

    public static enum Type {
        PHONE(DbValue.PHONE_DB_VALUE),
        ;
        public final String dbValue;

        Type(String dbValue) {
            this.dbValue = dbValue;
        }
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "contact_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "contact_id_generator", sequenceName = "contact_id_seq", allocationSize = 1)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt")
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt")
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return copy(created);
    }

    public Date getUpdated() {
        return copy(updated);
    }

    public abstract Type getType();

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
        if (!(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        if (getId() != null ? !getId().equals(contact.getId()) : contact.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
