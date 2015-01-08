package bynull.realty.grabber.json;

import bynull.realty.dto.vk.ItemDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by trierra on 12/20/14.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Getter
@Setter
public class ItemJSON {

    private Long id;

    @JsonProperty("id")
    private String formId;

    @JsonProperty("owner_id")
    private String ownerId;

    private Date date;

    private List<AttachmentJSON> attachmentJSONs;

    public ItemJSON() {
    }

    public static ItemJSON from(ItemDTO model) {
        if (model == null) return null;

        ItemJSON json = new ItemJSON();
        json.setDate(model.getDate());
        json.setFormId(model.getFormId());
        json.setOwnerId(model.getOwnerId());
        json.setAttachmentJSONs(model.getAttachmentDTOs().stream().map(AttachmentJSON::from).collect(Collectors.toList()));
        json.setId(model.getId());

        return json;
    }

    public ItemDTO toDto() {
        ItemDTO dto = new ItemDTO();
        dto.setFormId(getFormId());
        dto.setOwnerId(getOwnerId());
        dto.setAttachmentDTOs(getAttachmentJSONs().stream().map(AttachmentJSON::toDto).collect(Collectors.toList()));
        dto.setDate(getDate());
        dto.setId(getId());
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemJSON)) return false;

        ItemJSON json = (ItemJSON) o;

        if (attachmentJSONs != null ? !attachmentJSONs.equals(json.attachmentJSONs) : json.attachmentJSONs != null)
            return false;
        if (date != null ? !date.equals(json.date) : json.date != null) return false;
        if (formId != null ? !formId.equals(json.formId) : json.formId != null) return false;
        if (ownerId != null ? !ownerId.equals(json.ownerId) : json.ownerId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = formId != null ? formId.hashCode() : 0;
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (attachmentJSONs != null ? attachmentJSONs.hashCode() : 0);
        return result;
    }
}
