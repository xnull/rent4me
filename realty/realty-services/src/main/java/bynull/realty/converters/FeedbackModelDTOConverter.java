package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.Feedback;
import bynull.realty.dto.FeedbackDTO;
import bynull.realty.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 4/11/15.
 */
@Component
public class FeedbackModelDTOConverter implements Converter<Feedback, FeedbackDTO> {
    @Override
    public FeedbackDTO newTargetType(Optional<Feedback> in) {
        return new FeedbackDTO();
    }

    @Override
    public Feedback newSourceType(FeedbackDTO in) {
        return new Feedback();
    }

    @Override
    public Optional<FeedbackDTO> toTargetType(Optional<Feedback> in, FeedbackDTO instance) {
        return in.flatMap(f -> {
            FeedbackDTO out = new FeedbackDTO();
            out.setText(f.getText());
            out.setEmail(f.getEmail());
            out.setName(f.getName());
            out.setId(f.getId());
            out.setUpdated(f.getUpdated());
            out.setCreated(f.getCreated());
            out.setCreator(UserDTO.from(f.getCreator()));
            return Optional.of(out);
        });
    }

    @Override
    public Feedback toSourceType(FeedbackDTO in, Feedback instance) {
        if (in == null) {
            return null;
        }
        Feedback out = new Feedback();
        out.setText(in.getText());
        out.setEmail(in.getEmail());
        out.setName(in.getName());
        out.setCreator(UserDTO.toReference(in.getCreator()));
        return out;
    }
}
