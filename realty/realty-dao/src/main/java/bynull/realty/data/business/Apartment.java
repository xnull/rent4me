package bynull.realty.data.business;

import bynull.realty.data.business.metro.MetroEntity;
import bynull.realty.data.common.GeoPoint;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by null on 21.06.14.
 */
@Entity
@Table(name = "apartments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "data_source", discriminatorType = DiscriminatorType.STRING)
public abstract class Apartment implements Serializable {

    public void applyDelta(ApartmentInfoDelta delta) {
        Assert.notNull(delta, "delta required in order to be applied");

        this.setLocation(delta.getLocation());
        this.setAddressComponents(delta.getAddressComponents());
        this.setRoomCount(delta.getRoomCount());
        this.setFloorNumber(delta.getFloorNumber());
        this.setFloorsTotal(delta.getFloorsTotal());
    }

    public static class DbValue {
        private DbValue() {
        }

        public static final String INTERNAL_STRING_DB_VALUE = "INTERNAL";
        public static final String FACEBOOK_STRING_DB_VALUE = "FB";
        public static final String VKONTAKTE_STRING_DB_VALUE = "VK";
    }

    public static enum  DataSource {
        INTERNAL(DbValue.INTERNAL_STRING_DB_VALUE),
        FACEBOOK(DbValue.FACEBOOK_STRING_DB_VALUE),
        VKONTAKTE(DbValue.VKONTAKTE_STRING_DB_VALUE),
        ;
        public final String dbValue;

        DataSource(String dbValue) {
            this.dbValue = dbValue;
        }
    }

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "logical_created_dt")
    private Date logicalCreated;

    @Column(name = "type_of_rent")
    @Enumerated(EnumType.STRING)
    private RentType typeOfRent;

    @Column(name = "rental_fee")
    private BigDecimal rentalFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_period")
    private FeePeriod feePeriod;

    @Column(name = "description")
    private String description;

    @Column(name = "room_count")
    private Integer roomCount;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "floors_total")
    private Integer floorsTotal;

    /**
     * Area in root meters
     */
    @Column(name = "area")
    private BigDecimal area;

    @Column(name = "published")
    private boolean published;

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
            name = "apartments_metros",
            joinColumns = @JoinColumn(name = "apartment_id"),
            inverseJoinColumns = @JoinColumn(name = "metro_station_id")
    )
    @OneToMany
    private Set<MetroEntity> metros;

    @Enumerated(EnumType.STRING)
    @Column(name = "target")
    private Target target;

    @Column(name = "description_hash")
    private String descriptionHash;

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

    public Date getLogicalCreated() {
        return copy(logicalCreated);
    }

    public void setLogicalCreated(Date logicalCreated) {
        this.logicalCreated = logicalCreated;
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

    public Set<MetroEntity> getMetros() {
        return metros;
    }

    public void setMetros(Set<MetroEntity> metros) {
        this.metros = metros;
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

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public abstract DataSource getDataSource();

    @PrePersist
    void prePersist() {
        Date date = new Date();
        setCreated(date);
        setUpdated(date);
        if(getLogicalCreated() == null) {
            setLogicalCreated(date);
        }
        String descr = getDescription();
        descriptionHash = calcHash(descr);

    }

    public static String calcHash(String descr) {
        return descr != null ? DigestUtils.sha512Hex(descr) : null;
    }

    @PreUpdate
    void preUpdate() {
        Date date = new Date();
        setUpdated(date);

        String descr = getDescription();
        descriptionHash = calcHash(descr);
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

    /**
     * Who is target for this apartment.
     */
    public static enum Target {
        RENTER,
        LESSOR,
        /**
         * Means it's for both - renter and lessor.
         */
        BOTH,
        UNKNOWN
    }
}
