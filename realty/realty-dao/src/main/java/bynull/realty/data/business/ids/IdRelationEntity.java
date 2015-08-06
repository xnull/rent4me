package bynull.realty.data.business.ids;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Created by null on 8/2/15.
 */
@ToString
@Getter
@Setter
@Table(name = "id_relations")
@Entity
public class IdRelationEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * IdentEntity id
     */
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * IdentEntity id
     */
    @Column(name = "adjacent_id")
    private Long adjacentId;
}
