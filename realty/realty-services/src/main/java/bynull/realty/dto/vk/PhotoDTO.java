package bynull.realty.dto.vk;

import bynull.realty.data.business.vk.Photo;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by trierra on 12/22/14.
 */
@Getter
@Setter
public class PhotoDTO {

    private Long id;

    private String photoId;

    private String albumId;

    private String ownerId;

    private String text;

    private String photoSrc;

    public PhotoDTO() {
    }

    public static PhotoDTO from(Photo photo) {
        if (photo == null) return null;

        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setPhotoSrc(photo.getPhotoSrc());
        photoDTO.setId(photo.getId());
        photoDTO.setText(photo.getText());
        photoDTO.setOwnerId(photo.getOwnerId());
        photoDTO.setAlbumId(photo.getAlbumId());
        photoDTO.setPhotoId(photo.getPhotoId());

        return photoDTO;
    }

    public Photo toInternal() {
        Photo photo = new Photo();
        photo.setId(getId());
        photo.setAlbumId(getAlbumId());
        photo.setOwnerId(getOwnerId());
        photo.setPhotoId(getPhotoId());
        photo.setText(getText());
        photo.setPhotoSrc(getPhotoSrc());

        return photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoDTO)) return false;

        PhotoDTO photoDTO = (PhotoDTO) o;

        if (albumId != null ? !albumId.equals(photoDTO.albumId) : photoDTO.albumId != null) return false;
        if (id != null ? !id.equals(photoDTO.id) : photoDTO.id != null) return false;
        if (ownerId != null ? !ownerId.equals(photoDTO.ownerId) : photoDTO.ownerId != null) return false;
        if (photoId != null ? !photoId.equals(photoDTO.photoId) : photoDTO.photoId != null) return false;
        if (photoSrc != null ? !photoSrc.equals(photoDTO.photoSrc) : photoDTO.photoSrc != null) return false;
        if (text != null ? !text.equals(photoDTO.text) : photoDTO.text != null) return false;

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
