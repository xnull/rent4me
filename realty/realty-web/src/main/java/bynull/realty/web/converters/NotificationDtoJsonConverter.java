package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.dto.NotificationDTO;
import bynull.realty.web.json.ChatMessageJSON;
import bynull.realty.web.json.NotificationJSON;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by dionis on 5/2/15.
 */
@Component
public class NotificationDtoJsonConverter implements Converter<NotificationDTO, NotificationJSON> {
    @Override
    public NotificationJSON newTargetType(Optional<NotificationDTO> in) {
        return new NotificationJSON();
    }

    @Override
    public NotificationDTO newSourceType(NotificationJSON in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<NotificationJSON> toTargetType(Optional<NotificationDTO> in, NotificationJSON instance) {
        return in.map(ntf -> {
            instance.setId(ntf.getId());
            instance.setResolved(ntf.isResolved());
            instance.setType(ntf.getType().getDbValue());
            instance.setCreated(ntf.getCreated());
            switch (ntf.getType()) {
                case NEW_MESSAGE: {
                    instance.setValue(ChatMessageJSON.from((ChatMessageDTO) ntf.getValue()));
                }
                break;
                default:
                    throw new UnsupportedOperationException("Unsupported conversion " + ntf.getType());
            }
            return instance;
        });
    }

    @Override
    public NotificationDTO toSourceType(NotificationJSON in, NotificationDTO instance) {
        throw new UnsupportedOperationException();
    }
}
