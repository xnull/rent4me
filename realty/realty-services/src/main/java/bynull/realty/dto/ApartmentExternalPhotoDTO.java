package bynull.realty.dto;

import bynull.realty.data.business.ApartmentExternalPhoto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dionis on 05/12/14.
 */
@Getter
@Setter
public class ApartmentExternalPhotoDTO {
    private String smallThumbnailUrl;
    private String fullPictureUrl;

    public static ApartmentExternalPhotoDTO from(ApartmentExternalPhoto apartmentPhoto) {
        if (apartmentPhoto == null) {
            return null;
        }
        ApartmentExternalPhotoDTO photoDTO = new ApartmentExternalPhotoDTO();

        photoDTO.setSmallThumbnailUrl(apartmentPhoto.getPreviewUrl());
        photoDTO.setFullPictureUrl(apartmentPhoto.getImageUrl());

        return photoDTO;
    }
}
