package bynull.realty.grabber.json;

import bynull.realty.dto.vk.PhotoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by trierra on 12/22/14.
 */
@Getter
@Setter
public class PhotoJSON {

    private Long id;

    @JsonProperty("id")
    private String photoId;

    @JsonProperty("album_id")
    private String albumId;

    @JsonProperty("owner_id")
    private String ownerId;

    private String text;

    @JsonProperty("photo_807")
    private String photoSrc;

    public PhotoJSON() {

    }

    public static PhotoJSON from(PhotoDTO dto) {

        if (dto == null) return null;

        PhotoJSON json = new PhotoJSON();
        json.setText(dto.getText());
        json.setPhotoSrc(dto.getPhotoSrc());
        json.setOwnerId(dto.getOwnerId());
        json.setAlbumId(dto.getAlbumId());
        json.setPhotoId(dto.getPhotoId());
        json.setId(dto.getId());
        return json;
    }

    public PhotoDTO toDto() {
        PhotoDTO dto = new PhotoDTO();
        dto.setPhotoId(getPhotoId());
        dto.setAlbumId(getAlbumId());
        dto.setText(getText());
        dto.setOwnerId(getOwnerId());
        dto.setPhotoSrc(getPhotoSrc());
        dto.setId(getId());
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoJSON)) return false;

        PhotoJSON json = (PhotoJSON) o;

        if (albumId != null ? !albumId.equals(json.albumId) : json.albumId != null) return false;
        if (id != null ? !id.equals(json.id) : json.id != null) return false;
        if (ownerId != null ? !ownerId.equals(json.ownerId) : json.ownerId != null) return false;
        if (photoId != null ? !photoId.equals(json.photoId) : json.photoId != null) return false;
        if (photoSrc != null ? !photoSrc.equals(json.photoSrc) : json.photoSrc != null) return false;
        if (text != null ? !text.equals(json.text) : json.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (photoId != null ? photoId.hashCode() : 0);
        result = 31 * result + (albumId != null ? albumId.hashCode() : 0);
        result = 31 * result + (ownerId != null ? ownerId.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (photoSrc != null ? photoSrc.hashCode() : 0);
        return result;
    }
}
