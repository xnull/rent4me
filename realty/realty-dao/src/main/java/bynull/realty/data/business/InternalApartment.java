package bynull.realty.data.business;

import bynull.realty.data.common.GeoPoint;
import bynull.realty.hibernate.validation.annotations.LessThanOrEqual;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by null on 21.06.14.
 */
@Entity
@DiscriminatorValue(Apartment.DbValue.INTERNAL_STRING_DB_VALUE)
@LessThanOrEqual(targetField = "floorNumber", fieldForComparison = "floorsTotal", message = "Количество этажей всего должно быть больше или равно указанному этажу")
public class InternalApartment extends Apartment {

    @NotNull
    @JoinColumn(name = "owner_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
            name = "apartment_apartment_photos",
            joinColumns = @JoinColumn(name = "apartment_id"),
            inverseJoinColumns = @JoinColumn(name = "apartment_photo_id")
    )
    @OneToMany
    private Set<ApartmentPhoto> photos;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void addApartmentPhoto(ApartmentPhoto photo) {
        Set<ApartmentPhoto> apartmentPhotos = this.photos;
        if (apartmentPhotos == null) {
            apartmentPhotos = new HashSet<>();
            this.photos = apartmentPhotos;
        }
        apartmentPhotos.add(photo);
    }

    public void deleteApartmentPhoto(ApartmentPhoto photo) {
        Set<ApartmentPhoto> apartmentPhotos = this.photos;
        if (apartmentPhotos == null) {
            return;
        }
        apartmentPhotos.remove(photo);
    }

    public List<ApartmentPhoto> listPhotosNewestFirst() {
        Set<ApartmentPhoto> photos = this.photos;
        if (photos == null) {
            return Collections.emptyList();
        } else {
            List<ApartmentPhoto> result = new ArrayList<>(photos);
            Collections.sort(result, new Comparator<ApartmentPhoto>() {
                @Override
                public int compare(ApartmentPhoto o1, ApartmentPhoto o2) {
                    return o2.getCreationTimestamp().compareTo(o1.getCreationTimestamp());
                }
            });
            return result;
        }
    }

    @NotNull
    @Override
    public GeoPoint getLocation() {
        return super.getLocation();
    }

    @NotNull
    @Override
    public AddressComponents getAddressComponents() {
        return super.getAddressComponents();
    }

    @NotNull
    @Override
    public RentType getTypeOfRent() {
        return super.getTypeOfRent();
    }


    @NotNull @Min(1)
    @Override
    public BigDecimal getRentalFee() {
        return super.getRentalFee();
    }


    @NotNull
    @Override
    public FeePeriod getFeePeriod() {
        return super.getFeePeriod();
    }


    @Size(max = 2000)
    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Min(1)
    @Override
    public Integer getRoomCount() {
        return super.getRoomCount();
    }

    @Min(1)
    @Override
    public Integer getFloorNumber() {
        return super.getFloorNumber();
    }

    @Min(1)
    @Override
    public Integer getFloorsTotal() {
        return super.getFloorsTotal();
    }

    @Min(1)
    @Override
    public BigDecimal getArea() {
        return super.getArea();
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.INTERNAL;
    }

    public void mergeWith(Apartment apartment) {
        mergeWith(apartment, true);
    }

    public void mergeWithRentInfoOnly(Apartment apartment) {
        mergeWith(apartment, false);
    }

    private void mergeWith(Apartment apartment, boolean updateAllInfo) {
        Assert.notNull(apartment);

        if (updateAllInfo) {
            setAddressComponents(apartment.getAddressComponents());
            setLocation(apartment.getLocation());
            setArea(apartment.getArea());
            setFloorNumber(apartment.getFloorNumber());
            setFloorsTotal(apartment.getFloorsTotal());
            setRoomCount(apartment.getRoomCount());
        }
        setDescription(apartment.getDescription());
        setFeePeriod(apartment.getFeePeriod());
        setRentalFee(apartment.getRentalFee());
        setTypeOfRent(apartment.getTypeOfRent());
        setPublished(apartment.isPublished());
    }
}
