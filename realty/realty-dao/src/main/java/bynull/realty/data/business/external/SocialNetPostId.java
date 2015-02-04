package bynull.realty.data.business.external;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

/**
 * Created by dionis on 04/02/15.
 */
@Getter
@EqualsAndHashCode
@Embeddable
public class SocialNetPostId implements Serializable {
    @Column(name = "post_id")
    private Long postId;
    @Enumerated(EnumType.STRING)
    @Column(name = "social_network", insertable = false, updatable = false)
    private SocialNetwork socialNetwork;

    public String toStringRepresentation() {
        return getPostId()+"_"+getSocialNetwork();
    }

}
