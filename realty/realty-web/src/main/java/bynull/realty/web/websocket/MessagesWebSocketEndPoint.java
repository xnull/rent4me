package bynull.realty.web.websocket;

import bynull.realty.components.api.ChatMessageUsersOnlineNotifier;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.dto.UserDTO;
import bynull.realty.services.api.UserService;
import bynull.realty.services.api.UserTokenService;
import bynull.realty.utils.JsonMapperException;
import bynull.realty.utils.JsonUtils;
import bynull.realty.web.json.ChatMessageJSON;
import com.amazonaws.util.json.JSONUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dionis on 4/29/15.
 */
@Component
@Slf4j
public class MessagesWebSocketEndPoint extends TextWebSocketHandler implements ChatMessageUsersOnlineNotifier {

    @Resource
    GenericWebSocketEndPoint genericWebSocketEndPoint;

    @Resource
    UserTokenService userTokenService;

    @Resource
    UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();



    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        log.warn("No handler worked");
    }

    @Override
    public Collection<Long> getUserIdsOnline() {
        return genericWebSocketEndPoint.getUserIdsOnline();
    }

    @Override
    public boolean isUserOnline(long userId) {
        return genericWebSocketEndPoint.isUserOnline(userId);
    }

    @Override
    public boolean sendToUserIfOnline(long userId, ChatMessageDTO content) {
        ChatMessageJSON chatMessageJSON = ChatMessageJSON.from(content);
        try {
            return genericWebSocketEndPoint.sendToUserIfOnline(userId, objectMapper.writeValueAsString(chatMessageJSON));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean broadcastToChannel(String channel, ChatMessageDTO content) {
        ChatMessageJSON chatMessageJSON = ChatMessageJSON.from(content);
        try {
            String json = JsonUtils.toJson(chatMessageJSON);
            return genericWebSocketEndPoint.broadcastToChannel(channel, json);
        } catch (JsonMapperException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean sendToUserOnChannel(String channel, long userId, ChatMessageDTO content) {
        ChatMessageJSON chatMessageJSON = ChatMessageJSON.from(content);
        try {
            String json = JsonUtils.toJson(chatMessageJSON);
            return genericWebSocketEndPoint.sendToUserOnChannel(channel, userId, json);
        } catch (JsonMapperException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessagesToParticipants(ChatMessageDTO chatMessage) {
        sendToUserOnChannel("messages", chatMessage.getReceiver().getId(), chatMessage);
    }

}
