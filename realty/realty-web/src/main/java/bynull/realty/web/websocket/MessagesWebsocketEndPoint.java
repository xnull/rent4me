package bynull.realty.web.websocket;

import bynull.realty.dto.UserDTO;
import bynull.realty.services.api.UserService;
import bynull.realty.services.api.UserTokenService;
import bynull.realty.utils.JsonMapperException;
import bynull.realty.utils.JsonUtils;
import bynull.realty.web.json.ChatMessageJSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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

/**
 * Created by dionis on 4/29/15.
 */
@Component
public class MessagesWebsocketEndPoint extends TextWebSocketHandler {

    Map<Long, Set<WebSocketSession>> webSocketSessions = new HashMap<>();

    @Resource
    UserTokenService userTokenService;

    @Resource
    UserService userService;

    private final Object lock = new Object();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    @Setter
    public static class WsAuthMessageJSON {
        @JsonProperty("type")
        private final String type = "ws_auth";
        @JsonProperty("username")
        private String username;
        @JsonProperty("token")
        private String token;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class WsAuthResponseJSON {
        @JsonProperty("status")
        private final Status status;

        public static enum Status {
            OK, NOK
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        SecurityUtils.UserIDHolder userIDHolder = SecurityUtils.getAuthorizedUser();
//        super.afterConnectionEstablished(session);
//        webSocketSessions.add(session);
//        System.out.println("Opened");
//        TextMessage returnMessage = new TextMessage("User joned. Users online: "+webSocketSessions.size());
//        sendMessagesToAll(returnMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        super.afterConnectionClosed(session, status);
//        webSocketSessions.remove(session);
//        System.out.println("Closed");
//        TextMessage returnMessage = new TextMessage("User left. Users online: "+webSocketSessions.size());
//        sendMessagesToAll(returnMessage);
        removeSession(session);
    }

    private void removeSession(WebSocketSession session) {
        synchronized (lock) {
            for (Map.Entry<Long, Set<WebSocketSession>> entry : webSocketSessions.entrySet()) {
                Set<WebSocketSession> value = entry.getValue();
                value.remove(session);
            }
        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        super.handleTransportError(session, exception);
//        webSocketSessions.remove(session);
//        System.out.println("Transport error");
//        TextMessage returnMessage = new TextMessage("User left. Users online: "+webSocketSessions.size());
//        sendMessagesToAll(returnMessage);
        removeSession(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);

        String payload = (String) message.getPayload();

        try {
            WsAuthMessageJSON authJson = objectMapper.readValue(payload, WsAuthMessageJSON.class);

            try {
                if(!userTokenService.isValidAuthentication(new UserService.UsernameTokenPair(authJson.getUsername(), authJson.getToken()))) {
                    throw new BadCredentialsException("Bad credentials for WS auth: "+payload);
                }

                Optional<UserDTO> byName = userService.findByUsername(authJson.getUsername());
                byName.ifPresent(user -> {
                    synchronized (lock) {
                        if (!webSocketSessions.containsKey(user.getId())) {
                            webSocketSessions.put(user.getId(), new HashSet<>());
                        }
                        webSocketSessions.get(user.getId()).add(session);
                    }
                });

                String responseJson = JsonUtils.toJson(new WsAuthResponseJSON(WsAuthResponseJSON.Status.OK));
                session.sendMessage(new TextMessage(responseJson));
            } catch (UsernameNotFoundException | BadCredentialsException e) {
                String responseJson = JsonUtils.toJson(new WsAuthResponseJSON(WsAuthResponseJSON.Status.NOK));
                session.sendMessage(new TextMessage(responseJson));
            }
            return;
        } catch (Exception e) {
//            e.printStackTrace();
        }

        try {
            ChatMessageJSON chatMessageJSON = objectMapper.readValue(payload, ChatMessageJSON.class);

            sendMessagesToAll(chatMessageJSON);
            return;
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void sendMessagesToAll(ChatMessageJSON chatMessage) throws IOException {
        try {
            String json = JsonUtils.toJson(chatMessage);
            TextMessage message = new TextMessage(json);
            Set<WebSocketSession> sessions = new HashSet<>();
            synchronized (lock) {
                sessions.addAll(webSocketSessions.getOrDefault(chatMessage.getSender().getId(), Collections.emptySet()));
                sessions.addAll(webSocketSessions.getOrDefault(chatMessage.getReceiver().getId(), Collections.emptySet()));
            }
            for (WebSocketSession webSocketSession : sessions) {
                if(webSocketSession.isOpen()){
                    try {
                        webSocketSession.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("closed. skipping");
                }
            }
        } catch (JsonMapperException e) {
            e.printStackTrace();
        }
    }
}
