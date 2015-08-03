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

import java.util.Optional;

/**
 * Created by dionis on 5/2/15.
 */
@Component
public class NotificationModelDTOConverter implements Converter<Notification, NotificationDTO> {
    @Override
    public NotificationDTO newTargetType(Optional<Notification> in) {
        return new NotificationDTO();
    }

    @Override
    public Notification newSourceType(NotificationDTO in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<NotificationDTO> toTargetType(Optional<Notification> in, NotificationDTO instance) {
        return in.flatMap(ntf -> {
            ntf = HibernateUtil.deproxy(in);
            instance.setId(ntf.getId());
            instance.setType(ntf.getType());
            instance.setReceiver(UserDTO.from(ntf.getReceiver()));
            instance.setResolved(ntf.isResolved());
            instance.setCreated(ntf.getCreated());

            switch (ntf.getType()) {
                case NEW_MESSAGE: {
                    NewMessageNotification newMessageNotification = (NewMessageNotification) ntf;
                    ChatMessage chatMessage = newMessageNotification.getChatMessage();
                    instance.setValue(ChatMessageDTO.from(chatMessage));
                }
                break;
                default:
                    throw new UnsupportedOperationException("Not supported conversion type");
            }

            return Optional.of(instance);
        });
    }

    @Override
    public Notification toSourceType(NotificationDTO in, Notification instance) {
        throw new UnsupportedOperationException();
    }
}
