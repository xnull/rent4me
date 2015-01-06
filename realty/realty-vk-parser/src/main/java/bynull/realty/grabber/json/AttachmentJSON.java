package bynull.realty.grabber.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by trierra on 12/20/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AttachmentJSON {

    String type;

    PhotoJSON photo;

    LinkJSON link;

    public AttachmentJSON() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PhotoJSON getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoJSON photo) {
        this.photo = photo;
    }

    public LinkJSON getLink() {
        return link;
    }

    public void setLink(LinkJSON link) {
        this.link = link;
    }
}
