package bynull.realty.data.business.vk;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by trierra on 12/20/14.
 */
@Entity
@Table(name = "items")
public class Item implements Serializable {
    @Id
    @GeneratedValue(generator = "item_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "item_id_generator", sequenceName = "item_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "form_id")
    private String fromId;
    @Column(name = "owner_id")
    private String ownerId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @OneToMany(mappedBy = "item")
    private List<Attachment> attachments;

    public Item() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String formId) {
        this.fromId = formId;
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
