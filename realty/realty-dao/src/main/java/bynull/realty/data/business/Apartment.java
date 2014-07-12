package bynull.realty.data.business;

import bynull.realty.data.common.GeoPoint;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by null on 21.06.14.
 */
@Entity
@Table(name = "apartments")
public class Apartment implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "apartment_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "apartment_id_generator", sequenceName = "apartment_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "location")
    private GeoPoint location;
    @Embedded
    private AddressComponents addressComponents;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt")
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt")
    private Date updated;

    @JoinColumn(name = "owner_id")
    @ManyToOne
    private User owner;
    @Column(name = "type_of_rent")
    @Enumerated(EnumType.STRING)
    private RentType typeOfRent;
    @Column(name = "rental_fee")
    private BigDecimal rentalFee;
    @Enumerated(EnumType.STRING)
    @Column(name = "fee_period")
    private FeePeriod feePeriod;
    @Column(name = "short_desc")
    private String shortDescription;
    @Column(name = "extended_desc")
    private String extendedDescription;

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

    public AddressComponents getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(AddressComponents addressComponents) {
        this.addressComponents = addressComponents;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public RentType getTypeOfRent() {
        return typeOfRent;
    }

    public void setTypeOfRent(RentType typeOfRent) {
        this.typeOfRent = typeOfRent;
    }

    public BigDecimal getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(BigDecimal rentalFee) {
        this.rentalFee = rentalFee;
    }

    public FeePeriod getFeePeriod() {
        return feePeriod;
    }

    public void setFeePeriod(FeePeriod feePeriod) {
        this.feePeriod = feePeriod;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getExtendedDescription() {
        return extendedDescription;
    }

    public void setExtendedDescription(String extendedDescription) {
        this.extendedDescription = extendedDescription;
    }

    public void updateFrom(Apartment apartment) {
//        this.setAddress(apartment.getAddress());
//        this.setCity(apartment.getCity());
        this.setCreated(apartment.getCreated());
        this.setUpdated(apartment.getUpdated());
        this.setLocation(apartment.getLocation());
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
