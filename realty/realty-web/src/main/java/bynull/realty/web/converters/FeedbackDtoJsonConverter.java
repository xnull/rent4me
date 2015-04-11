package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.Feedback;
import bynull.realty.dto.FeedbackDTO;
import bynull.realty.dto.UserDTO;
import bynull.realty.web.json.FeedbackJSON;
import bynull.realty.web.json.UserJSON;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 4/11/15.
 */
@Component
public class FeedbackDtoJsonConverter implements Converter<FeedbackDTO, FeedbackJSON> {
    @Override
    public FeedbackJSON newTargetType(FeedbackDTO in) {
        return new FeedbackJSON();
    }

    @Override
    public FeedbackDTO newSourceType(FeedbackJSON in) {
        return new FeedbackDTO();
    }

    @Override
    public FeedbackJSON toTargetType(FeedbackDTO in, FeedbackJSON instance) {
        if (in == null) {
            return null;
        }
        FeedbackJSON out = new FeedbackJSON();
        out.setText(in.getText());
        out.setId(in.getId());
        out.setUpdated(in.getUpdated());
        out.setCreated(in.getCreated());
        out.setCreator(UserJSON.from(in.getCreator()));
        return out;
    }

    @Override
    public FeedbackDTO toSourceType(FeedbackJSON in, FeedbackDTO instance) {
        if (in == null) {
            return null;
        }
        FeedbackDTO out = new FeedbackDTO();
        out.setText(in.getText());
        out.setId(in.getId());
        out.setUpdated(in.getUpdated());
        out.setCreated(in.getCreated());
        out.setCreator(UserJSON.toReference(in.getCreator()));
        return out;
    }
}
