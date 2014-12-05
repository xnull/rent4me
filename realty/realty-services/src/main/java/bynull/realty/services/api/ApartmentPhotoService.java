package bynull.realty.services.api;

import bynull.realty.dto.ApartmentPhotoDTO;

import java.util.List;

/**
 * @author dionis on 05/12/14.
 */
public interface ApartmentPhotoService {
    String createPhotoTemp(byte[] content);

    List<ApartmentPhotoDTO> findPlaceProfilePhotosByGUIDs(List<String> guids);
}
