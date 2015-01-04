package bynull.realty.dto;

import java.util.Date;
import java.util.List;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 04/01/15.
 */
public class FacebookPostDTO {
    private String message;
    private List<String> imageUrls;
    private String link;
    private Date created;
    private Date updated;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getCreated() {
        return copy(created);
    }

    public void setCreated(Date created) {
        this.created = copy(created);
    }

    public Date getUpdated() {
        return copy(updated);
    }

    public void setUpdated(Date updated) {
        this.updated = copy(updated);
    }
}
