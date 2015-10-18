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
@Table(name = IdentEntity.T_IDENT)
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class IdentEntity {
    public static final String T_IDENT = "ident";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident_value")
    private String identValue;

    @Column(name = "id_type")
    private String identType;
}
