package bynull.realty.services.api;

import bynull.realty.dto.PhotoTempDTO;

import java.util.List;

/**
 * @author dionis on 05/12/14.
 */
public interface PhotoTempService {
    /**
     * Create photo temp.
     * @param url url to be saved in temp
     * @param guid
     * @return guid for this photo temp.
     */
    String createPhotoTemp(String url, String guid);

    void collectGarbage();

    List<PhotoTempDTO> findPhotoTempByGUIDs(List<String> tempPhotoGUIDs);
}
