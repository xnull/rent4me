package bynull.realty.services.impl;

import bynull.realty.converters.FeedbackModelDTOConverter;
import bynull.realty.dao.FeedbackRepository;
import bynull.realty.data.business.Feedback;
import bynull.realty.dto.FeedbackDTO;
import bynull.realty.dto.UserDTO;
import bynull.realty.services.api.FeedbackService;
import bynull.realty.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 4/11/15.
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Resource
    FeedbackRepository feedbackRepository;

    @Resource
    FeedbackModelDTOConverter feedbackModelDTOConverter;

    @Transactional
    @Override
    public void createFeedback(FeedbackDTO feedbackDTO) {
        Assert.notNull(feedbackDTO);

        Optional<SecurityUtils.UserIDHolder> authorizedUserOptional = SecurityUtils.getAuthorizedUserOptional();

        authorizedUserOptional.ifPresent(it-> {
            UserDTO dto = new UserDTO();
            dto.setId(it.getId());
            feedbackDTO.setCreator(dto);
        });

        Feedback feedback = feedbackModelDTOConverter.toSourceType(feedbackDTO);


        feedback = feedbackRepository.saveAndFlush(feedback);
    }
}
