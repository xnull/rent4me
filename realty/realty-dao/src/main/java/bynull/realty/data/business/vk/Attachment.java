package bynull.realty.data.business.vk;

import javax.persistence.*;

/**
 * Created by trierra on 12/20/14.
 */
@Entity
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(generator = "attachment_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "attachment_id_generator", sequenceName = "attachment_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "attachment_type")
    private String type;
    @Embedded
    private Photo photo;
    @Embedded
    private Link link;

    @JoinColumn(name = "item_id")
    @ManyToOne
    private Item item;

    public Attachment() {
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
