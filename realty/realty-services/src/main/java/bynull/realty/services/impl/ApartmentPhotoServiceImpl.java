package bynull.realty.services.impl;

import bynull.realty.components.AfterCommitExecutor;
import bynull.realty.components.ImageComponent;
import bynull.realty.dao.ApartmentPhotoRepository;
import bynull.realty.dao.PhotoTempRepository;
import bynull.realty.data.business.ApartmentPhoto;
import bynull.realty.data.business.PhotoTemp;
import bynull.realty.dto.ApartmentPhotoDTO;
import bynull.realty.services.api.ApartmentPhotoService;
import bynull.realty.services.api.PhotoTempService;

import bynull.realty.utils.SecurityUtils;
import bynull.realty.utils.URLUtil;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Resource
    AfterCommitExecutor afterCommitExecutor;

    @Transactional
    @Override
    public String createPhotoTemp(byte[] content) {
        Assert.notNull(content);
        String guid = UUID.randomUUID().toString();
        String jpegURL = imageComponent.createJpeg(guid, content);
        String photoTempGuid = photoTempService.createPhotoTemp(jpegURL, guid);
        return photoTempGuid;
    }

    @Transactional
    @Override
    public ApartmentPhotoDTO createApartmentPhotoWithThumbnails(PhotoTemp photoTemp) {
        Assert.notNull(photoTemp);
        SecurityUtils.verifySameUser(photoTemp.getAuthor());
        ApartmentPhoto photo = new ApartmentPhoto();

        //small thumbnail handler
        {
//          TODO: move creation of http client to another component because it's pretty expensive operation to create each time new one.
            byte[] bytes = URLUtil.fetchExternalResourceContentOrNull(new HttpClient(), photoTemp.getUrl());

            byte[] smallThumbnail = imageComponent.createSmallThumbnail(bytes);
            String smallThumbnailImageId = UUID.randomUUID().toString();
            String smallThumbnailJpeg = imageComponent.createJpeg(smallThumbnailImageId, smallThumbnail);
            photo.setSmallThumbnailObjectId(smallThumbnailImageId);
            photo.setSmallThumbnailUrl(smallThumbnailJpeg);
        }

        //original
        {
//            String mainJpeg = imageComponent.createJpeg(imageId, bytes);
            photo.setOriginalImageObjectId(photoTemp.getGuid());
            photo.setOriginalImageUrl(photoTemp.getUrl());
        }

        photo.setGuid(photoTemp.getGuid());
        photo.setAuthor(photoTemp.getAuthor());
        photo = apartmentPhotoRepository.saveAndFlush(photo);
        return ApartmentPhotoDTO.from(photo);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ApartmentPhotoDTO> findPlaceProfilePhotosByGUIDs(List<String> guids) {
        Assert.notNull(guids);
        if(guids.isEmpty()) return Collections.emptyList();
        return apartmentPhotoRepository.findByGuidIn(guids)
                    .stream()
                    .map(ApartmentPhotoDTO::from)
                    .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteAll(List<ApartmentPhotoDTO> list) {
        Assert.notNull(list);
        if(list.isEmpty()) return;

        List<String> guids = list.stream()
                    .map(ApartmentPhotoDTO::getGuid)
                    .collect(Collectors.toList());

        List<ApartmentPhoto> apartmentPhotos = apartmentPhotoRepository.findByGuidIn(guids);

        for (ApartmentPhoto apartmentPhoto : apartmentPhotos) {
            SecurityUtils.verifySameUser(apartmentPhoto.getAuthor());
            afterCommitExecutor.execute(() -> imageComponent.deleteJpegSilently(apartmentPhoto.getSmallThumbnailObjectId()));
            afterCommitExecutor.execute(() -> imageComponent.deleteJpegSilently(apartmentPhoto.getOriginalImageObjectId()));
        }

        apartmentPhotoRepository.deleteInBatch(apartmentPhotos);
    }
}
