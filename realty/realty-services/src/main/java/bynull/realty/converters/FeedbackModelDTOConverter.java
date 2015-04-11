package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.Feedback;
import bynull.realty.dto.FeedbackDTO;
import bynull.realty.dto.UserDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 4/11/15.
 */
@Component
public class FeedbackModelDTOConverter implements Converter<Feedback, FeedbackDTO> {
    @Override
    public FeedbackDTO newTargetType(Feedback in) {
        return new FeedbackDTO();
    }

    @Override
    public Feedback newSourceType(FeedbackDTO in) {
        return new Feedback();
    }

    @Override
    public FeedbackDTO toTargetType(Feedback in, FeedbackDTO instance) {
        if (in == null) {
            return null;
        }
        FeedbackDTO out = new FeedbackDTO();
        out.setText(in.getText());
        out.setId(in.getId());
        out.setUpdated(in.getUpdated());
        out.setCreated(in.getCreated());
        out.setCreator(UserDTO.from(in.getCreator()));
        return out;
    }

    @Override
    public Feedback toSourceType(FeedbackDTO in, Feedback instance) {
        if (in == null) {
            return null;
        }
        Feedback out = new Feedback();
        out.setText(in.getText());
        out.setCreator(UserDTO.toReference(in.getCreator()));
        return out;
    }
}
