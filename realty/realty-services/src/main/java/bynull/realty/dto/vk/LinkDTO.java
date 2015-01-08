package bynull.realty.dto.vk;

import bynull.realty.data.business.vk.Link;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by trierra on 12/22/14.
 */
@Getter
@Setter
public class LinkDTO {

    private Long id;
    private String description;
    private String imageSrc;
    private String title;
    private String url;

    public LinkDTO() {
    }


    public Link toInternal() {
        Link link = new Link();
        link.setTitle(getTitle());
        link.setId(getId());
        link.setDescription(getDescription());
        link.setUrl(getUrl());
        link.setImageSrc(getImageSrc());

        return link;
    }

    public static LinkDTO from(Link link) {
        if (link == null) return null;
        LinkDTO linkDTO = new LinkDTO();
        linkDTO.setId(link.getId());
        linkDTO.setUrl(link.getUrl());
        linkDTO.setImageSrc(link.getImageSrc());
        linkDTO.setTitle(link.getTitle());
        linkDTO.setDescription(link.getDescription());
        return linkDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkDTO)) return false;

        LinkDTO linkDTO = (LinkDTO) o;

        if (description != null ? !description.equals(linkDTO.description) : linkDTO.description != null) return false;
        if (id != null ? !id.equals(linkDTO.id) : linkDTO.id != null) return false;
        if (imageSrc != null ? !imageSrc.equals(linkDTO.imageSrc) : linkDTO.imageSrc != null) return false;
        if (title != null ? !title.equals(linkDTO.title) : linkDTO.title != null) return false;
        if (url != null ? !url.equals(linkDTO.url) : linkDTO.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (imageSrc != null ? imageSrc.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
