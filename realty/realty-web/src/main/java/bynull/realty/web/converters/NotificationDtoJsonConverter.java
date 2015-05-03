package bynull.realty.web.converters;

import bynull.realty.common.Converter;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.dto.NotificationDTO;
import bynull.realty.web.json.ChatMessageJSON;
import bynull.realty.web.json.NotificationJSON;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 5/2/15.
 */
@Component
public class NotificationDtoJsonConverter implements Converter<NotificationDTO, NotificationJSON> {
    @Override
    public NotificationJSON newTargetType(NotificationDTO in) {
        return new NotificationJSON();
    }

    @Override
    public NotificationDTO newSourceType(NotificationJSON in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NotificationJSON toTargetType(NotificationDTO in, NotificationJSON instance) {
        if (in == null) {
            return null;
        }
        instance.setId(in.getId());
        instance.setResolved(in.isResolved());
        instance.setType(in.getType().getDbValue());
        instance.setCreated(in.getCreated());
        switch (in.getType()) {
            case NEW_MESSAGE:{
                instance.setValue(ChatMessageJSON.from((ChatMessageDTO) in.getValue()));
            }
            break;
            default:
                throw new UnsupportedOperationException("Unsupported conversion "+in.getType());
        }
        return instance;
    }

    @Override
    public NotificationDTO toSourceType(NotificationJSON in, NotificationDTO instance) {
        throw new UnsupportedOperationException();
    }
}
