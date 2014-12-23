package bynull.realty.crawler.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by trierra on 12/22/14.
 */
public class Photo extends BaseEntity {
    @JsonProperty("id")
    private Long photoId;

    @JsonProperty("album_id")
    private Long albumId;

    @JsonProperty("owner_id")
    private Long ownerId;

    private String text;
    @JsonProperty("photo_807")
    private String photoSrc;

    public Photo() {
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
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
