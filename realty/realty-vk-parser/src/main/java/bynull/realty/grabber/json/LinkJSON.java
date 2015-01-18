package bynull.realty.grabber.json;

import bynull.realty.dto.vk.LinkDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by trierra on 12/22/14.
 */
@Getter
@Setter
public class LinkJSON {
    private Long entityId;
    private String description;

    @JsonProperty("image_src")
    private String imageSrc;

    private String title;

    private String url;

    public LinkJSON() {
    }


    public static LinkJSON from(LinkDTO model) {
        if (model == null) return null;
        LinkJSON json = new LinkJSON();
        json.setEntityId(model.getId());
        json.setDescription(model.getDescription());
        json.setImageSrc(model.getImageSrc());
        json.setTitle(model.getTitle());
        json.setUrl(model.getUrl());

        return json;
    }

    public LinkDTO toDto() {
        LinkDTO dto = new LinkDTO();
        dto.setImageSrc(getImageSrc());
        dto.setId(getEntityId());
        dto.setUrl(getUrl());
        dto.setDescription(getDescription());
        dto.setTitle(getTitle());
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkJSON)) return false;

        LinkJSON json = (LinkJSON) o;

        if (description != null ? !description.equals(json.description) : json.description != null) return false;
        if (entityId != null ? !entityId.equals(json.entityId) : json.entityId != null) return false;
        if (imageSrc != null ? !imageSrc.equals(json.imageSrc) : json.imageSrc != null) return false;
        if (title != null ? !title.equals(json.title) : json.title != null) return false;
        if (url != null ? !url.equals(json.url) : json.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = entityId != null ? entityId.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imageSrc != null ? imageSrc.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
