package bynull.realty.services.api;

import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.dto.NotificationDTO;

import java.util.List;
import java.util.Set;

/**
 * Created by dionis on 5/2/15.
 */
public interface NotificationService {
    void createNewChatMessageNotification(ChatMessageDTO chatMessageDTO);

    List<? extends NotificationDTO> listMyUnreadNotifications();

    void resolveMyNotifications(Set<Long> notificationIds);
}
