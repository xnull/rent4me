package bynull.realty.dto.vk;

import bynull.realty.data.business.vk.Attachment;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by trierra on 12/20/14.
 */
@Getter
@Setter
public class AttachmentDTO {
    private Long id;
    private String type;
    private PhotoDTO photoDTO;
    private LinkDTO linkDTO;

    public AttachmentDTO() {
    }

    public static AttachmentDTO from(Attachment model) {
        if (model == null) return null;
        AttachmentDTO dto = new AttachmentDTO();
        dto.setPhotoDTO(PhotoDTO.from(model.getPhoto()));
        dto.setType(model.getType());
        dto.setLinkDTO(LinkDTO.from(model.getLink()));

        return dto;
    }

    public Attachment toInternal() {
        Attachment attachment = new Attachment();
        attachment.setType(getType());
        attachment.setLink(linkDTO != null ? linkDTO.toInternal() : null);
        attachment.setPhoto(photoDTO != null ? photoDTO.toInternal() : null);

        return attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttachmentDTO)) return false;

        AttachmentDTO dto = (AttachmentDTO) o;

        if (id != null ? !id.equals(dto.id) : dto.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
