package bynull.realty.data.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 08.01.15.
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = CityEntity.TABLE)
public class CityEntity {

    public static final String TABLE = "cities";
    private static final String ID_GEN = TABLE + "_id_generator";
    private static final String ID_SEQ = TABLE + "_id_seq";

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = ID_GEN, strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = ID_GEN, sequenceName = ID_SEQ, allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;
}
