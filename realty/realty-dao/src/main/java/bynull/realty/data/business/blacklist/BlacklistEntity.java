package bynull.realty.data.business.blacklist;

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
public class BlacklistEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@SequenceGenerator(name = "blacklist_id_generator", sequenceName = "blacklist_id_seq", allocationSize = 1)
    private Long id;

    //@OneToOne(fetch=FetchType.LAZY)
    //@JoinColumn(name="identification_id")
    //private Identification identification;

    //@Column(name="ident_id", updatable=false, insertable=false)
    @Column(name="ident_id")
    private Long identId;

    //@OneToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name="apartment_id")
    //private Apartment apartment;

    //@Column(name="apartment_id", updatable=false, insertable=false)
    //private Long apartmentId;
}
