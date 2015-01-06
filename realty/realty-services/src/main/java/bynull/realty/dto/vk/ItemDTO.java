package bynull.realty.dto.vk;

import bynull.realty.data.business.vk.Item;

import java.util.Date;
import java.util.List;

/**
 * Created by trierra on 12/20/14.
 */
public class ItemDTO extends BaseEntity {
    Long formId;

    Long ownerId;

    Date date;

    List<AttachmentDTO> attachmentDTOs;

    public ItemDTO() {
    }

    public static ItemDTO from(Item model) {
        if (model == null) return null;
        ItemDTO dto = new ItemDTO();
        //TODO: to complete
        return dto;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<AttachmentDTO> getAttachmentDTOs() {
        return attachmentDTOs;
    }

    public void setAttachmentDTOs(List<AttachmentDTO> attachmentDTOs) {
        this.attachmentDTOs = attachmentDTOs;
    }
}
