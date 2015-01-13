package bynull.realty.grabber.json;

import bynull.realty.dto.vk.AttachmentDTO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by trierra on 12/20/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Getter
@Setter
public class AttachmentJSON {

    private Long id;
    private String type;
    private PhotoJSON photo;
    private LinkJSON link;

    public AttachmentJSON() {
    }

    public static AttachmentJSON from(AttachmentDTO dto) {
        if (dto == null) return null;

        AttachmentJSON json = new AttachmentJSON();
        json.setType(dto.getType());
        json.setLink(dto.getLinkDTO() != null ? LinkJSON.from(dto.getLinkDTO()) : null);
        json.setPhoto(dto.getPhotoDTO() != null ? PhotoJSON.from(dto.getPhotoDTO()) : null);
        json.setId(dto.getId());
        return json;
    }

    public AttachmentDTO toDto() {
        AttachmentDTO dto = new AttachmentDTO();
        dto.setType(getType());
        dto.setLinkDTO(getLink().toDto());
        dto.setPhotoDTO(getPhoto().toDto());
        dto.setId(getId());
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttachmentJSON)) return false;

        AttachmentJSON json = (AttachmentJSON) o;

        if (id != null ? !id.equals(json.id) : json.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
