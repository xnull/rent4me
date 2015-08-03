package bynull.realty.web.websocket;

import bynull.realty.common.JsonUtils;
import bynull.realty.common.JsonUtils.JsonMapperException;
import bynull.realty.components.api.NotificationUsersOnlineNotifier;
import bynull.realty.dto.NotificationDTO;
import bynull.realty.services.api.UserService;
import bynull.realty.services.api.UserTokenService;
import bynull.realty.web.converters.NotificationDtoJsonConverter;
import bynull.realty.web.json.NotificationJSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by dionis on 4/29/15.
 */
@Component
@Slf4j
public class NotificationsWebSocketEndPoint implements NotificationUsersOnlineNotifier {

    @Resource
    GenericWebSocketEndPoint genericWebSocketEndPoint;

    @Resource
    UserTokenService userTokenService;

    @Resource
    UserService userService;

    @Resource
    NotificationDtoJsonConverter notificationDtoJsonConverter;

    @Override
    public void deliverNotification(NotificationDTO notificationDTO) {
        NotificationJSON jsonObject = notificationDtoJsonConverter.toTargetType(Optional.ofNullable(notificationDTO)).get();
        try {
            String json = JsonUtils.toJson(jsonObject);
            genericWebSocketEndPoint.sendToUserOnChannel("notifications", notificationDTO.getReceiver().getId(), json);
        } catch (JsonMapperException e) {
            throw new RuntimeException(e);
        }
    }
}
