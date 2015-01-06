package bynull.realty.data.business.vk;

import java.util.Date;
import java.util.List;

/**
 * Created by trierra on 12/20/14.
 */
public class Item extends BaseEntity {
    Long formId;
    Long ownerId;
    Date date;
    List<Attachment> attachments;

    public Item() {
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
