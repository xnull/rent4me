package bynull.realty.data.business.vk;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by trierra on 12/22/14.
 */
@Embeddable
public class Link implements Serializable {
    private String description;
    @Column(name = "image_src")
    private String imageSrc;
    private String title;
    @Column(name = "link_url")
    private String url;

    public Link() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
