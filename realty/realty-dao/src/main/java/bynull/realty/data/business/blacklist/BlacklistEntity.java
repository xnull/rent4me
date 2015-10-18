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
    private Long id;

    @Column(name="ident_id")
    private Long identId;
}
