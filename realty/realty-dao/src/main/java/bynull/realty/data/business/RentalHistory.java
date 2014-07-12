package bynull.realty.data.business;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 25/06/14.
 */
@Entity
@Table(name = "rental_histories")
public class RentalHistory implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "rent_history_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "rent_history_id_generator", sequenceName = "rental_history_id_seq", allocationSize = 1)
    private Long id;

    @JoinColumn(name = "apartment_id")
    @ManyToOne
    private Apartment apartment;
    /**
     * One who don't own property and takes it in rent from another one
     */
    @JoinColumn(name = "rentee_id")
    @ManyToOne
    private User rentee;
    /**
     * One who owns property - denormalized
     */
    @JoinColumn(name = "owner_id")
    @ManyToOne
    private User owner;
    @Temporal(TemporalType.DATE)
    @Column(name = "rent_start")
    private Date start;
    @Temporal(TemporalType.DATE)
    @Column(name = "rent_end")
    private Date end;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated")
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public User getRentee() {
        return rentee;
    }

    public void setRentee(User rentee) {
        this.rentee = rentee;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getStart() {
        return copy(start);
    }

    public void setStart(Date start) {
        this.start = copy(start);
    }

    public Date getEnd() {
        return copy(end);
    }

    public void setEnd(Date end) {
        this.end = copy(end);
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
        setUpdated(new Date());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RentalHistory)) return false;

        RentalHistory that = (RentalHistory) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
