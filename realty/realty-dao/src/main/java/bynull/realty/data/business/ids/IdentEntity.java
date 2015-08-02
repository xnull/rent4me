package bynull.realty.data.business.ids;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Идентификатор чего либо
 * Created by null on 8/2/15.
 */
@Table(name = "ident")
@Entity
@Getter
@Setter
public class IdentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "id_type")
    private String type;

}
