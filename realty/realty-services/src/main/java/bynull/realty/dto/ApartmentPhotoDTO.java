package bynull.realty.dto;

import bynull.realty.data.business.ApartmentPhoto;

/**
 * @author dionis on 05/12/14.
 */
public class ApartmentPhotoDTO {
    private Long id;
    private String smallThumbnailUrl;
    private String fullPictureUrl;
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
