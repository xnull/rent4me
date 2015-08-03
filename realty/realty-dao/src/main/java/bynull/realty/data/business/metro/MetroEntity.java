package bynull.realty.data.business.metro;

import bynull.realty.data.common.CityEntity;
import bynull.realty.data.common.GeoPoint;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Optional;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 08.01.15.
 */
@Entity
@Table(name = MetroEntity.TABLE)
@Setter
@Getter
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MetroEntity implements Serializable {
    public static final String TABLE = "metro_stations";
    private static final String ID_GEN = TABLE + "_id_generator";
    private static final String ID_SEQ = TABLE + "_id_seq";

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = ID_GEN, strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = ID_GEN, sequenceName = ID_SEQ, allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String stationName;

    @Column(name = "location", nullable = false)
    @NotNull
    private GeoPoint location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity city;

    public Optional<CityEntity> getCity(){
        return Optional.ofNullable(city);
    }
}
