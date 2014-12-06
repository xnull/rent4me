package bynull.realty.web.json;

import bynull.realty.dto.ApartmentPhotoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author dionis on 05/12/14.
 */
public class ApartmentPhotoJSON {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("small_thumbnail_url")
    private String smallThumbnailUrl;
    @JsonProperty("full_picture_url")
    private String fullPictureUrl;
    @JsonProperty("guid")
    private String guid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmallThumbnailUrl() {
        return smallThumbnailUrl;
    }

    public void setSmallThumbnailUrl(String smallThumbnailUrl) {
        this.smallThumbnailUrl = smallThumbnailUrl;
    }

    public String getFullPictureUrl() {
        return fullPictureUrl;
    }

    public void setFullPictureUrl(String fullPictureUrl) {
        this.fullPictureUrl = fullPictureUrl;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

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
