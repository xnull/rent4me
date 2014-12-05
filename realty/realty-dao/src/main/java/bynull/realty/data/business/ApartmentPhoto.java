package bynull.realty.data.business;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author dionis on 05/12/14.
 */
@Entity
@Table(name = "apartment_photos")
public class ApartmentPhoto {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "apartment_photos_id_generator")
    @SequenceGenerator(sequenceName = "apartment_photos_id_seq", name = "apartment_photos_id_generator", allocationSize = 1)
    private Long id;

    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    /**
     * Full image url
     */
    @Column(name = "original_image_url", nullable = false)
    private String originalImageUrl;

    @Column(name = "original_image_object_id", nullable = false)
    private String originalImageObjectId;

    @Column(name = "small_thumbnail_url", nullable = false)
    private String smallThumbnailUrl;

    @Column(name = "small_thumbnail_object_id", nullable = false)
    private String smallThumbnailObjectId;

    /**
     * Uniquely identifies this place profile photo(could be treated as second id).
     * <br/>
     * Also it is an implicit reference to {@linkplain bynull.realty.data.business.PhotoTemp guid} entirely unique across all system.<br/>
     * <br/>
     * <b>NB!</b> Because of it's nature PhotoTemp will be "garbage collected", but guid will be still present in this entity.
     *
     * @see bynull.realty.data.business.PhotoTemp#guid PhotoTemp guid
     */
    @Column(name = "guid", nullable = false)
    private String guid;


    @Column(name = "creation_dtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTimestamp;

    @PrePersist
    void prePersist() {

        creationTimestamp = new Date();

        //if guid was not set by default - generate new one. Needed for backward compatibility
        if(getGuid() == null) {

            setGuid(UUID.randomUUID().toString());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getOriginalImageUrl() {
        return originalImageUrl;
    }

    public void setOriginalImageUrl(String originalImageUrl) {
        this.originalImageUrl = originalImageUrl;
    }

    public String getOriginalImageObjectId() {
        return originalImageObjectId;
    }

    public void setOriginalImageObjectId(String originalImageObjectId) {
        this.originalImageObjectId = originalImageObjectId;
    }

    public String getSmallThumbnailUrl() {
        return smallThumbnailUrl;
    }

    public void setSmallThumbnailUrl(String smallThumbnailUrl) {
        this.smallThumbnailUrl = smallThumbnailUrl;
    }

    public String getSmallThumbnailObjectId() {
        return smallThumbnailObjectId;
    }

    public void setSmallThumbnailObjectId(String smallThumbnailObjectId) {
        this.smallThumbnailObjectId = smallThumbnailObjectId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }


}
