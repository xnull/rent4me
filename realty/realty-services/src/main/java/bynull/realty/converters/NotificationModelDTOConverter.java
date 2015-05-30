package bynull.realty.converters;

import bynull.realty.common.Converter;
import bynull.realty.data.business.chat.ChatMessage;
import bynull.realty.data.business.notifications.NewMessageNotification;
import bynull.realty.data.business.notifications.Notification;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.dto.NotificationDTO;
import bynull.realty.dto.UserDTO;
import bynull.realty.utils.HibernateUtil;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 5/2/15.
 */
@Component
public class NotificationModelDTOConverter implements Converter<Notification, NotificationDTO> {
    @Override
    public NotificationDTO newTargetType(Notification in) {
        return new NotificationDTO();
    }

    @Override
    public Notification newSourceType(NotificationDTO in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public NotificationDTO toTargetType(Notification in, NotificationDTO instance) {
        if (in == null) {
            return null;
        }
        in = HibernateUtil.deproxy(in);
        instance.setId(in.getId());
        instance.setType(in.getType());
        instance.setReceiver(UserDTO.from(in.getReceiver()));
        instance.setResolved(in.isResolved());
        instance.setCreated(in.getCreated());

        switch (in.getType()) {
            case NEW_MESSAGE: {
                NewMessageNotification newMessageNotification = (NewMessageNotification) in;
                ChatMessage chatMessage = newMessageNotification.getChatMessage();
                instance.setValue(ChatMessageDTO.from(chatMessage));
            }
            break;
            default:
                throw new UnsupportedOperationException("Not supported conversion type");
        }

        return instance;
    }

    @Override
    public Notification toSourceType(NotificationDTO in, Notification instance) {
        throw new UnsupportedOperationException();
    }
}
