package bynull.realty.data.business.ids;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Идентификатор чего либо
 * Created by null on 8/2/15.
 */
@Table(name = "ident")
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
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
