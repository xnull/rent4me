package bynull.realty.services.api;

import bynull.realty.data.business.ApartmentPhoto;
import bynull.realty.data.business.PhotoTemp;
import bynull.realty.dto.ApartmentPhotoDTO;

import java.util.List;

/**
 * @author dionis on 05/12/14.
 */
public interface ApartmentPhotoService {
    String createPhotoTemp(byte[] content);

    ApartmentPhotoDTO createApartmentPhotoWithThumbnails(PhotoTemp photoTemp);

    List<ApartmentPhotoDTO> findPlaceProfilePhotosByGUIDs(List<String> guids);

    void deleteAll(List<ApartmentPhotoDTO> list);
}
