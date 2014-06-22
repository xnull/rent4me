package bynull.realty.data.business;

import bynull.realty.data.common.GeoPoint;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by null on 21.06.14.
 */
@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "apartment_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "apartment_id_generator", sequenceName = "apartment_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "location")
    private GeoPoint location;
    @Column(name = "city")
    private String city;
    @Column(name = "address")
    private String address;
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

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
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
        if (!(o instanceof Apartment)) return false;

        Apartment apartment = (Apartment) o;

        if (getId() != null ? !getId().equals(apartment.getId()) : apartment.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
