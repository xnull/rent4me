package bynull.realty.data.business;

import bynull.realty.data.common.GeoPoint;
import bynull.realty.hibernate.validation.annotations.LessThanOrEqual;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by null on 21.06.14.
 */
@Entity
@Table(name = "apartments")
@LessThanOrEqual(targetField = "floorNumber", fieldForComparison = "floorsTotal", message = "Количество этажей всего должно быть больше или равно указанному этажу")
public class Apartment implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "apartment_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "apartment_id_generator", sequenceName = "apartment_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "location")
    @NotNull
    private GeoPoint location;
    @NotNull
    @Embedded
    private AddressComponents addressComponents;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_dt")
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_dt")
    private Date updated;

    @NotNull
    @JoinColumn(name = "owner_id")
    @ManyToOne
    private User owner;

    @NotNull
    @Column(name = "type_of_rent")
    @Enumerated(EnumType.STRING)
    private RentType typeOfRent;

    @Min(1)
    @NotNull
    @Column(name = "rental_fee")
    private BigDecimal rentalFee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "fee_period")
    private FeePeriod feePeriod;

    @Column(name = "description")
    private String description;

    @NotNull
    @Min(1)
    @Column(name = "room_count")
    private Integer roomCount;

    @Min(1)
    @Column(name = "floor_number")
    private Integer floorNumber;

    @Min(1)
    @Column(name = "floors_total")
    private Integer floorsTotal;

    @Min(1)
    @Column(name = "area")
    private BigDecimal area;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void updateFrom(Apartment apartment) {
//        this.setAddress(apartment.getAddress());
//        this.setCity(apartment.getCity());
        this.setCreated(apartment.getCreated());
        this.setUpdated(apartment.getUpdated());
        this.setLocation(apartment.getLocation());
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Integer getFloorsTotal() {
        return floorsTotal;
    }

    public void setFloorsTotal(Integer floorsTotal) {
        this.floorsTotal = floorsTotal;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
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
