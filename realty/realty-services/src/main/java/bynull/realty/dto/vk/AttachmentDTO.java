package bynull.realty.dto.vk;

/**
 * Created by trierra on 12/20/14.
 */
public class AttachmentDTO extends BaseEntity {
    String type;
    PhotoDTO photoDTO;
    LinkDTO linkDTO;

    public AttachmentDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PhotoDTO getPhotoDTO() {
        return photoDTO;
    }

    public void setPhotoDTO(PhotoDTO photoDTO) {
        this.photoDTO = photoDTO;
    }

    public LinkDTO getLinkDTO() {
        return linkDTO;
    }

    public void setLinkDTO(LinkDTO linkDTO) {
        this.linkDTO = linkDTO;
    }
}
