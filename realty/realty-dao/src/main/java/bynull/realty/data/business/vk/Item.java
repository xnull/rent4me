package bynull.realty.data.business.vk;

import java.util.Date;
import java.util.List;

/**
 * Created by trierra on 12/20/14.
 */
public class Item extends BaseEntity {
    String formId;
    String ownerId;
    Date date;
    List<Attachment> attachments;

    public Item() {
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
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
