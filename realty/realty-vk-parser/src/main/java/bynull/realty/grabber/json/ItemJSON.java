package bynull.realty.grabber.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * Created by trierra on 12/20/14.
 */
public class ItemJSON extends BaseEntity {
    @JsonProperty("id")
    Long formId;

    @JsonProperty("owner_id")
    Long ownerId;

    Date date;

    List<AttachmentJSON> attachmentJSONs;

    public ItemJSON() {
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

    public List<AttachmentJSON> getAttachmentJSONs() {
        return attachmentJSONs;
    }

    public void setAttachmentJSONs(List<AttachmentJSON> attachmentJSONs) {
        this.attachmentJSONs = attachmentJSONs;
    }
}
