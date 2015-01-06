package bynull.realty.dto;

import bynull.realty.data.business.ApartmentPhoto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dionis on 05/12/14.
 */
@Getter
@Setter
public class ApartmentPhotoDTO {
    private Long id;
    private String smallThumbnailUrl;
    private String fullPictureUrl;
    private String guid;

    public static ApartmentPhotoDTO from(ApartmentPhoto apartmentPhoto) {
        if (apartmentPhoto == null) {
            return null;
        }
        ApartmentPhotoDTO photoDTO = new ApartmentPhotoDTO();

        photoDTO.setId(apartmentPhoto.getId());
        photoDTO.setGuid(apartmentPhoto.getGuid());
        photoDTO.setSmallThumbnailUrl(apartmentPhoto.getSmallThumbnailUrl());
        photoDTO.setFullPictureUrl(apartmentPhoto.getOriginalImageUrl());

        return photoDTO;
    }
}
