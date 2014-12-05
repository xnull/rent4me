package bynull.realty.data.business;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author dionis on 05/12/14.
 */
@Entity
@Table(name = "photos_temp")
public class PhotoTemp {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "photo_temp_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "photo_temp_id_generator", sequenceName = "photos_temp_seq", allocationSize = 1)
    private Long id;
    @Column(name = "url")
    private String url;
    /**
     * This guid is also implicit reference to aws s3 object id.
     */
    @Column(name = "guid")
    private String guid;
    @Column(name = "created")
    private Date created;
    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User author;

    public Long getId() {
        return id;
    }

    void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getCreated() {
        return created;
    }

    void setCreated(Date created) {
        this.created = created;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @PrePersist
    void prePersist() {
        setCreated(new Date());
        if(getGuid() == null) {
            setGuid(UUID.randomUUID().toString());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoTemp)) return false;

        PhotoTemp photoTemp = (PhotoTemp) o;

        if (getId() != null ? !getId().equals(photoTemp.getId()) : photoTemp.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
