package bynull.realty.data.business.vk;

/**
 * Created by trierra on 12/20/14.
 */
public class Attachment extends BaseEntity {

    String type;
    Photo photo;
    Link link;

    public Attachment() {
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
