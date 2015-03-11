package bynull.realty.web.json;

import bynull.realty.dto.ApartmentExternalPhotoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dionis on 05/12/14.
 */
@Getter
@Setter
public class ApartmentExternalPhotoJSON {

    @JsonProperty("small_thumbnail_url")
    private String smallThumbnailUrl;
    @JsonProperty("full_picture_url")
    private String fullPictureUrl;

    public static ApartmentExternalPhotoJSON from(ApartmentExternalPhotoDTO apartmentPhoto) {
        if (apartmentPhoto == null) {
            return null;
        }
        ApartmentExternalPhotoJSON photoDTO = new ApartmentExternalPhotoJSON();

        photoDTO.setSmallThumbnailUrl(apartmentPhoto.getSmallThumbnailUrl());
        photoDTO.setFullPictureUrl(apartmentPhoto.getFullPictureUrl());

        return photoDTO;
    }
}
