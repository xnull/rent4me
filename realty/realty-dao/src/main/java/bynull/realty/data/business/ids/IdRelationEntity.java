package bynull.realty.data.business.ids;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by null on 8/2/15.
 */
@Getter
@Setter
@Table(name = "id_relations")
@Entity
public class IdRelationEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "adjacent_id")
    private Long adjacentId;
}
