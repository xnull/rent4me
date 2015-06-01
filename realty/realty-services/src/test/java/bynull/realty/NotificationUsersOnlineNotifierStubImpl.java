package bynull.realty;

import bynull.realty.components.api.NotificationUsersOnlineNotifier;
import bynull.realty.dto.NotificationDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 01/06/15.
 */
@Component
public class NotificationUsersOnlineNotifierStubImpl implements NotificationUsersOnlineNotifier {
    @Override
    public void deliverNotification(NotificationDTO notificationDTO) {

    }
}
