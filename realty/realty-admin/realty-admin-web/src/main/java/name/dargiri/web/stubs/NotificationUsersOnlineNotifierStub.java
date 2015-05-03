package name.dargiri.web.stubs;

import bynull.realty.components.api.NotificationUsersOnlineNotifier;
import bynull.realty.dto.NotificationDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 5/2/15.
 */
@Component
public class NotificationUsersOnlineNotifierStub implements NotificationUsersOnlineNotifier {
    @Override
    public void deliverNotification(NotificationDTO notificationDTO) {

    }
}
