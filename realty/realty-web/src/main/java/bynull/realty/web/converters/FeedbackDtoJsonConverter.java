package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.Feedback;
import bynull.realty.dto.FeedbackDTO;
import bynull.realty.dto.UserDTO;
import bynull.realty.web.json.FeedbackJSON;
import bynull.realty.web.json.UserJSON;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 4/11/15.
 */
@Component
public class FeedbackDtoJsonConverter implements Converter<FeedbackDTO, FeedbackJSON> {
    @Override
    public FeedbackJSON newTargetType(Optional<FeedbackDTO> in) {
        return new FeedbackJSON();
    }

    @Override
    public FeedbackDTO newSourceType(FeedbackJSON in) {
        return new FeedbackDTO();
    }

    @Override
    public Optional<FeedbackJSON> toTargetType(Optional<FeedbackDTO> in, FeedbackJSON instance) {
        return in.map(feedback -> {
            FeedbackJSON out = new FeedbackJSON();
            out.setText(feedback.getText());
            out.setEmail(feedback.getEmail());
            out.setName(feedback.getName());
            out.setId(feedback.getId());
            out.setUpdated(feedback.getUpdated());
            out.setCreated(feedback.getCreated());
            out.setCreator(UserJSON.from(feedback.getCreator()));
            return out;
        });
    }

    @Override
    public FeedbackDTO toSourceType(FeedbackJSON in, FeedbackDTO instance) {
        if (in == null) {
            return null;
        }
        FeedbackDTO out = new FeedbackDTO();
        out.setText(in.getText());
        out.setEmail(in.getEmail());
        out.setName(in.getName());
        out.setId(in.getId());
        out.setUpdated(in.getUpdated());
        out.setCreated(in.getCreated());
        out.setCreator(UserJSON.toReference(in.getCreator()));
        return out;
    }
}
