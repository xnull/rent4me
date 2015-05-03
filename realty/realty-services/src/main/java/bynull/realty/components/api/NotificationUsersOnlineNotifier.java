package bynull.realty.components.api;

import bynull.realty.dto.NotificationDTO;

/**
 * Created by dionis on 5/2/15.
 */
public interface NotificationUsersOnlineNotifier {
    void deliverNotification(NotificationDTO notificationDTO);
}
