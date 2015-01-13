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
        photoDTO.setText(photo.getText());
        photoDTO.setOwnerId(photo.getOwnerId());
        photoDTO.setAlbumId(photo.getAlbumId());
        photoDTO.setPhotoId(photo.getPhotoId());

        return photoDTO;
    }

    public Photo toInternal() {
        Photo photo = new Photo();
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

        if (id != null ? !id.equals(photoDTO.id) : photoDTO.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
