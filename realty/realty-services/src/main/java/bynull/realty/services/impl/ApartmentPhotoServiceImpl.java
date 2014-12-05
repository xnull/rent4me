package bynull.realty.services.impl;

import bynull.realty.components.ImageComponent;
import bynull.realty.dao.ApartmentPhotoRepository;
import bynull.realty.dao.PhotoTempRepository;
import bynull.realty.dto.ApartmentPhotoDTO;
import bynull.realty.services.api.ApartmentPhotoService;
import bynull.realty.services.api.PhotoTempService;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author dionis on 05/12/14.
 */
@Service
public class ApartmentPhotoServiceImpl implements ApartmentPhotoService {
    @Resource
    ImageComponent imageComponent;

    @Resource
    PhotoTempService photoTempService;

    @Resource
    ApartmentPhotoRepository apartmentPhotoRepository;

    @Resource
    PhotoTempRepository photoTempRepository;

    @Override
    public String createPhotoTemp(byte[] content) {
        Assert.notNull(content);
        String guid = UUID.randomUUID().toString();
        String jpegURL = imageComponent.createJpeg(guid, content);
        String photoTempGuid = photoTempService.createPhotoTemp(jpegURL, guid);
        return photoTempGuid;
    }

    @Override
    public List<ApartmentPhotoDTO> findPlaceProfilePhotosByGUIDs(List<String> guids) {
        Assert.notNull(guids);
        if(guids.isEmpty()) return Collections.emptyList();
        return apartmentPhotoRepository.findByGuidIn(guids)
                    .stream()
                    .map(ApartmentPhotoDTO::from)
                    .collect(Collectors.toList());
    }
}
