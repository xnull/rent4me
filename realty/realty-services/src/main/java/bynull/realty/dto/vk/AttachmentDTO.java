package bynull.realty.dto.vk;

import bynull.realty.data.business.vk.Attachment;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by trierra on 12/20/14.
 */
@Getter
@Setter
public class AttachmentDTO extends BaseEntity {
    private String type;
    private PhotoDTO photoDTO;
    private LinkDTO linkDTO;

    public AttachmentDTO() {
    }

    public AttachmentDTO from(Attachment model) {
        if (model == null) return null;
        AttachmentDTO dto = new AttachmentDTO();
        dto.setPhotoDTO(PhotoDTO.from(model.getPhoto()));
        dto.setType(model.getType());
        dto.setId(model.getId());
        dto.setLinkDTO(LinkDTO.from(model.getLink()));

        return dto;
    }

    public Attachment toInternal() {
        Attachment attachment = new Attachment();
        attachment.setType(getType());
        attachment.setLink(linkDTO != null ? linkDTO.toInternal() : null);
        attachment.setPhoto(photoDTO != null ? photoDTO.toInternal() : null);
        attachment.setId(getId());

        return attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttachmentDTO)) return false;

        AttachmentDTO dto = (AttachmentDTO) o;

        if (linkDTO != null ? !linkDTO.equals(dto.linkDTO) : dto.linkDTO != null) return false;
        if (photoDTO != null ? !photoDTO.equals(dto.photoDTO) : dto.photoDTO != null) return false;
        if (type != null ? !type.equals(dto.type) : dto.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (photoDTO != null ? photoDTO.hashCode() : 0);
        result = 31 * result + (linkDTO != null ? linkDTO.hashCode() : 0);
        return result;
    }
}
