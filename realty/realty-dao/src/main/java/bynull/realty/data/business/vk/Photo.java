package bynull.realty.data.business.vk;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by trierra on 12/22/14.
 */
@Getter
@Setter
public class Photo extends BaseEntity {
    private String photoId;
    private String albumId;
    private String ownerId;
    private String text;
    private String photoSrc;

    public Photo() {
    }
}
