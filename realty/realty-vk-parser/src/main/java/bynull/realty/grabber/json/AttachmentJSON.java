package bynull.realty.grabber.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by trierra on 12/20/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AttachmentJSON extends BaseEntity {
    String type;
    Photo photo;
    Link link;

    public AttachmentJSON() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
