package bynull.realty.data.business;

import bynull.realty.data.common.GeoPoint;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * @author dionis on 08/12/14.
 */
@Entity
@Table(name = "apartment_deltas")
public class ApartmentInfoDelta {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "apartment_deltas_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "apartment_deltas_id_generator", sequenceName = "apt_deltas_id_seq", allocationSize = 1)
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

    @Column(name = "applied")
    private boolean applied;

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

    @JoinColumn(name = "apartment_id")
    @ManyToOne()
    private Apartment apartment;

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

    public boolean isApplied() {
        return applied;
    }

    public void setApplied(boolean applied) {
        this.applied = applied;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
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
        if (!(o instanceof ApartmentInfoDelta)) return false;

        ApartmentInfoDelta apartmentInfoDelta = (ApartmentInfoDelta) o;

        if (getId() != null ? !getId().equals(apartmentInfoDelta.getId()) : apartmentInfoDelta.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
