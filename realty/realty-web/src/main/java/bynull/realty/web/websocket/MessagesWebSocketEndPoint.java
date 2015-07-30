package bynull.realty.web.websocket;

import bynull.realty.common.JsonUtils;
import bynull.realty.common.JsonUtils.JsonMapperException;
import bynull.realty.components.api.ChatMessageUsersOnlineNotifier;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.services.api.UserService;
import bynull.realty.services.api.UserTokenService;
import bynull.realty.web.json.ChatMessageJSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by dionis on 4/29/15.
 */
@Component
@Slf4j
public class MessagesWebSocketEndPoint implements ChatMessageUsersOnlineNotifier {

    @Resource
    GenericWebSocketEndPoint genericWebSocketEndPoint;

    @Resource
    UserTokenService userTokenService;

    @Resource
    UserService userService;

    @Override
    public void sendMessagesToParticipants(ChatMessageDTO chatMessage) {
        ChatMessageJSON chatMessageJSON = ChatMessageJSON.from(chatMessage);
        try {
            String json = JsonUtils.toJson(chatMessageJSON);
            genericWebSocketEndPoint.sendToUserOnChannel("messages", chatMessage.getReceiver().getId(), json);
        } catch (JsonMapperException e) {
            throw new RuntimeException(e);
        }
    }

}
