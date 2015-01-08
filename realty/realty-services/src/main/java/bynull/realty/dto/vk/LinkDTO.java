package bynull.realty.dto.vk;

import bynull.realty.data.business.vk.Link;

/**
 * Created by trierra on 12/22/14.
 */
public class LinkDTO extends BaseEntity {
    private String description;

    private String imageSrc;

    private String title;
    private String url;

    public LinkDTO() {
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

    public Link toInternal() {
        //TODO: complete

        return null;
    }

    public static LinkDTO from(Link link) {
        return null;
    }
}
