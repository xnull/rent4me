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
        link.setDescription(getDescription());
        link.setUrl(getUrl());
        link.setImageSrc(getImageSrc());

        return link;
    }

    public static LinkDTO from(Link link) {
        if (link == null) return null;
        LinkDTO linkDTO = new LinkDTO();
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

        if (id != null ? !id.equals(linkDTO.id) : linkDTO.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
