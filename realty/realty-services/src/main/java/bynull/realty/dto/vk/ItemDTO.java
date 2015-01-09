package bynull.realty.dto.vk;

import bynull.realty.data.business.vk.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by trierra on 12/20/14.
 */
@Getter
@Setter
public class ItemDTO {
    private Long id;
    private String formId;
    private String ownerId;
    private Date date;
    private List<AttachmentDTO> attachmentDTOs;

    public ItemDTO() {

    }

    public static ItemDTO from(Item item) {
        if (item == null) return null;
        ItemDTO dto = new ItemDTO();
        dto.setDate(item.getDate());
        dto.setId(item.getId());
        dto.setFormId(item.getFormId());
        dto.setOwnerId(item.getOwnerId());
        dto.setAttachmentDTOs(item.getAttachments() != null ? item.getAttachments().stream().map(AttachmentDTO::from).collect(Collectors.toList()) : null);
        return dto;
    }

    public Item toInternal() {
        Item item = new Item();
        item.setId(getId());
        item.setOwnerId(getOwnerId());
        item.setFormId(getFormId());
        item.setDate(getDate());
        item.setAttachments(getAttachmentDTOs().stream().map(AttachmentDTO::toInternal).collect(Collectors.toList()));
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemDTO)) return false;

        ItemDTO dto = (ItemDTO) o;

        if (attachmentDTOs != null ? !attachmentDTOs.equals(dto.attachmentDTOs) : dto.attachmentDTOs != null)
            return false;
        if (date != null ? !date.equals(dto.date) : dto.date != null) return false;
        if (formId != null ? !formId.equals(dto.formId) : dto.formId != null) return false;
        if (id != null ? !id.equals(dto.id) : dto.id != null) return false;
        if (ownerId != null ? !ownerId.equals(dto.ownerId) : dto.ownerId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (formId != null ? formId.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (attachmentDTOs != null ? attachmentDTOs.hashCode() : 0);
        return result;
    }
}
