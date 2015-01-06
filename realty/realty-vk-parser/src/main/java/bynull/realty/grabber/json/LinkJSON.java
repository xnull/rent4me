package bynull.realty.grabber.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by trierra on 12/22/14.
 */
public class LinkJSON {

    private String description;

    @JsonProperty("image_src")
    private String imageSrc;

    private String title;

    private String url;

    public LinkJSON() {
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
