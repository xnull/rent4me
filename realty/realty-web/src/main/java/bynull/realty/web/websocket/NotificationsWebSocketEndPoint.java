package bynull.realty.web.websocket;

import bynull.realty.components.api.ChatMessageUsersOnlineNotifier;
import bynull.realty.components.api.NotificationUsersOnlineNotifier;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.dto.NotificationDTO;
import bynull.realty.services.api.UserService;
import bynull.realty.services.api.UserTokenService;
import bynull.realty.utils.JsonMapperException;
import bynull.realty.utils.JsonUtils;
import bynull.realty.web.converters.NotificationDtoJsonConverter;
import bynull.realty.web.json.ChatMessageJSON;
import bynull.realty.web.json.NotificationJSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
        NotificationJSON jsonObject = notificationDtoJsonConverter.toTargetType(notificationDTO);
        try {
            String json = JsonUtils.toJson(jsonObject);
            genericWebSocketEndPoint.sendToUserOnChannel("notifications", notificationDTO.getReceiver().getId(), json);
        } catch (JsonMapperException e) {
            throw new RuntimeException(e);
        }
    }
}