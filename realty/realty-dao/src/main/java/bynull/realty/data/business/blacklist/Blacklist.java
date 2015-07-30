package bynull.realty.data.business.blacklist;

import bynull.realty.data.business.Apartment;
import bynull.realty.data.business.Identification;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by null on 7/27/15.
 */
@Setter
@Getter
@Entity
@Table(name = "blacklist")
public class Blacklist {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "blacklist_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "blacklist_id_generator", sequenceName = "blacklist_id_seq", allocationSize = 1)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="identification_id")
    private Identification identification;

    @Column(name="identification_id", updatable=false, insertable=false)
    private Long identificationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="apartment_id")
    private Apartment apartment;

    @Column(name="apartment_id", updatable=false, insertable=false)
    private Long apartmentId;
}
