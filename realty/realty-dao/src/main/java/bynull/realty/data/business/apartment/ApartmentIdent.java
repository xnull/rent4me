package bynull.realty.data.business.apartment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by null on 8/17/15.
 */
@ToString
@Entity
@Table(name = "apartment_ident")
@Getter
@Setter
public class ApartmentIdent {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "apartment_id")
    private Long apartmentId;

    /**
     * IdentEntity id
     */
    @Column(name = "ident_id")
    private Long identId;

}
