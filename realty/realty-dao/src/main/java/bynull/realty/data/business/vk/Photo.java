package bynull.realty.data.business.vk;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by trierra on 12/22/14.
 */
@Embeddable
public class Photo implements Serializable {

    @Column(name = "photo_id")
    private String photoId;
    @Column(name = "album_id")
    private String albumId;
    @Column(name = "owner_id")
    private String ownerId;

    private String text;
    @Column(name = "photo_src")
    private String photoSrc;

    public Photo() {
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(String photoSrc) {
        this.photoSrc = photoSrc;
    }
}
