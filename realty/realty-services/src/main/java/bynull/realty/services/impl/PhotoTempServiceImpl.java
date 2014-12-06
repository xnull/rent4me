package bynull.realty.services.impl;

import bynull.realty.components.AfterCommitExecutor;
import bynull.realty.components.ImageComponent;
import bynull.realty.dao.PhotoTempRepository;
import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.PhotoTemp;
import bynull.realty.data.business.User;
import bynull.realty.dto.ApartmentPhotoDTO;
import bynull.realty.dto.PhotoTempDTO;
import bynull.realty.services.api.ApartmentPhotoService;
import bynull.realty.services.api.PhotoTempService;
import bynull.realty.utils.SecurityUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author dionis on 05/12/14.
 */
@Service
public class PhotoTempServiceImpl implements PhotoTempService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoTempServiceImpl.class);

    @Resource
    PhotoTempRepository photoTempRepository;

    @Resource
    ApartmentPhotoService apartmentPhotoService;
    @Resource
    AfterCommitExecutor afterCommitExecutor;
    @Resource
    ImageComponent imageComponent;

    @Resource
    UserRepository userRepository;

    @Transactional
    @Override
    public String createPhotoTemp(String url, String guid) {
        Assert.notNull(url);
        User author = userRepository.findOne(SecurityUtils.getAuthorizedUser().getId());
        Assert.notNull(author);
        PhotoTemp photoTemp = new PhotoTemp();
        photoTemp.setGuid(guid);
        photoTemp.setUrl(url);
        photoTemp.setAuthor(author);
        photoTemp = photoTempRepository.saveAndFlush(photoTemp);
        return photoTemp.getGuid();
    }

    @Transactional
    @Override
    public void collectGarbage() {
        //garbage collect all the temp stuff that was created more than 1 day ago.
        Date date = new DateTime().minusMinutes(1).toDate();
        LOGGER.info("Finding GC candidates that were created before [{}]", date);
        List<PhotoTemp> gcCandidates = photoTempRepository.findPlacesForGarbageCollection(date);
        LOGGER.info("Found [{}] gc candidates", gcCandidates.size());
        List<String> guidsOfGCCandidates = gcCandidates.stream().map(PhotoTemp::getGuid).collect(Collectors.toList());

        List<ApartmentPhotoDTO> apartmentPhotos = apartmentPhotoService.findPlaceProfilePhotosByGUIDs(guidsOfGCCandidates);

        Set<String> idsThatShouldNotBeDeleted = apartmentPhotos.stream()
                .map(ApartmentPhotoDTO::getGuid)
                .collect(Collectors.toSet());



        LOGGER.info("Images from [{}] gc candidates should not be removed", idsThatShouldNotBeDeleted.size());
        for (PhotoTemp gcCandidate : gcCandidates) {
            final String guid = gcCandidate.getGuid();
            if(!idsThatShouldNotBeDeleted.contains(guid)) {
                LOGGER.info("GC'ing image from Amazon S3 with id [{}]", guid);
                afterCommitExecutor.execute(() -> imageComponent.deleteJpegSilently(guid));
            } else {
                LOGGER.info("NOT GC'ing image from Amazon S3 with id [{}] because it has link to apartment photo", guid);
            }
        }

        photoTempRepository.deleteInBatch(gcCandidates);
    }

    @Override
    public List<PhotoTempDTO> findPhotoTempByGUIDs(List<String> addedTempPhotosGUIDs) {
        if(addedTempPhotosGUIDs.isEmpty()) return Collections.emptyList();
        List<PhotoTemp> result = photoTempRepository.findByGuidIn(addedTempPhotosGUIDs);
        return result.stream().map(PhotoTempDTO::from).collect(Collectors.toList());
    }
}
