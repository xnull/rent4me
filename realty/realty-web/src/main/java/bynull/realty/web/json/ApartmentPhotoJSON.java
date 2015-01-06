package bynull.realty.web.json;

import bynull.realty.dto.ApartmentPhotoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dionis on 05/12/14.
 */
@Getter
@Setter
public class ApartmentPhotoJSON {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("small_thumbnail_url")
    private String smallThumbnailUrl;
    @JsonProperty("full_picture_url")
    private String fullPictureUrl;
    @JsonProperty("guid")
    private String guid;

    public static ApartmentPhotoJSON from(ApartmentPhotoDTO apartmentPhoto) {
        if (apartmentPhoto == null) {
            return null;
        }
        ApartmentPhotoJSON photoDTO = new ApartmentPhotoJSON();

        photoDTO.setId(apartmentPhoto.getId());
        photoDTO.setGuid(apartmentPhoto.getGuid());
        photoDTO.setSmallThumbnailUrl(apartmentPhoto.getSmallThumbnailUrl());
        photoDTO.setFullPictureUrl(apartmentPhoto.getFullPictureUrl());

        return photoDTO;
    }
}
