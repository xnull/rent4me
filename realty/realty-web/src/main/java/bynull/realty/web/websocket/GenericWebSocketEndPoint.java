package bynull.realty.web.websocket;

import bynull.realty.common.JsonUtils;
import bynull.realty.components.api.UsersOnlineNotifier;
import bynull.realty.dto.UserDTO;
import bynull.realty.services.api.UserService;
import bynull.realty.services.api.UserTokenService;
import bynull.realty.utils.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class GenericWebSocketEndPoint extends TextWebSocketHandler implements UsersOnlineNotifier<String> {

    private final Map<Long, Set<WebSocketSession>> webSocketSessions = new HashMap<>();
    private final Map<String, Set<Long>> channels = new HashMap<>();

    private final Object lock = new Object();

    public Map<Long, Set<WebSocketSession>> getWebSocketSessions() {
        return webSocketSessions;
    }

    public Object getLock() {
        return lock;
    }

    @Resource
    UserTokenService userTokenService;

    @Resource
    UserService userService;

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
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        removeSession(session);
    }

    private void removeSession(WebSocketSession session) {
        synchronized (lock) {
            Iterator<Map.Entry<Long, Set<WebSocketSession>>> wsIterator = webSocketSessions.entrySet().iterator();
            while (wsIterator.hasNext()) {
                Map.Entry<Long, Set<WebSocketSession>> entry = wsIterator.next();
                long userId = entry.getKey();
                Set<WebSocketSession> value = entry.getValue();
                value.remove(session);
                if(value.isEmpty()) {
                    wsIterator.remove();
                    Iterator<Map.Entry<String, Set<Long>>> channelsIterator = channels.entrySet().iterator();
                    while (channelsIterator.hasNext()) {
                        Map.Entry<String, Set<Long>> channelEntry = channelsIterator.next();
                        log.info("Un-subscribing from channel implicitly [{}]", channelEntry.getKey());
                        Set<Long> channelSubscribers = channelEntry.getValue();
                        channelSubscribers.remove(userId);
                        if(channelSubscribers.isEmpty()) {
                            channelsIterator.remove();
                        }
                    }
                }
            }
        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        removeSession(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        boolean present = SecurityUtils.getAuthorizedUserOptional().isPresent();
        log.info("Connection established for authorized user? [{}]", present);
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

        String subscriptionPrefix = "sub,";
        boolean subscribe = payload.startsWith(subscriptionPrefix);
        if(subscribe) {
            String channelName = payload.substring(subscriptionPrefix.length());
            log.info("Subscribing to channel [{}]", channelName);
            synchronized (lock) {
                if(!channels.containsKey(channelName)){
                    channels.put(channelName, new HashSet<>());
                }
                channels.get(channelName).add(findUserIdBySession(session));
            }
        } else {
            String unSubscriptionPrefix = "uns,";
            boolean unSubscribe = payload.startsWith(unSubscriptionPrefix);
            if(unSubscribe) {
                String channelName = payload.substring(unSubscriptionPrefix.length());
                log.info("Un-subscribing from channel [{}]", channelName);
                synchronized (lock) {
                    if (channels.containsKey(channelName)) {
                        Set<Long> userIds = channels.get(channelName);
                        userIds.remove(findUserIdBySession(session));
                        if (userIds.isEmpty()) {
                            channels.remove(channelName);
                        }
                    }
                }
            } else {

                log.warn("No handler worked for message [{}]", payload);
            }
        }
    }

    private long findUserIdBySession(WebSocketSession session) {
        synchronized (lock) {
            for (Map.Entry<Long, Set<WebSocketSession>> entry : webSocketSessions.entrySet()) {
                if(entry.getValue().contains(session)){
                    return entry.getKey();
                }
            }
        }
        throw new IllegalStateException("web socket sessions doesn't contain provided session");
    }

    @Override
    public Collection<Long> getUserIdsOnline() {
        final Set<Long> usersOnline;
        synchronized (lock) {
            usersOnline = webSocketSessions.entrySet().stream()
                    .filter(e -> !e.getValue().isEmpty() && e.getValue().stream().anyMatch(WebSocketSession::isOpen))
                    .map(Map.Entry::getKey).collect(Collectors.toSet());
        }

        return usersOnline;
    }

    @Override
    public boolean isUserOnline(long userId) {
        return getUserIdsOnline().contains(userId);
    }

    @Override
    public boolean sendToUserIfOnline(long userId, String content) {
        if(isUserOnline(userId)) {
            Set<WebSocketSession> sessions;
            synchronized (lock) {
                sessions = new HashSet<>(webSocketSessions.getOrDefault(userId, Collections.emptySet()));
            }
            sendToTargetSessions(sessions, content);
            return !sessions.isEmpty();
        } else {
            return false;
        }
    }

    private void sendToTargetSessions(Set<WebSocketSession> sessions, String content) {
        for (WebSocketSession session : sessions) {
            if(session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(content));
                } catch (IOException e) {
                    log.error("Exception occurred while sending message", e);
                }
            }
        }
    }

    @Override
    public boolean broadcastToChannel(String channel, String content) {
        Set<WebSocketSession> sessions = new HashSet<>();
        synchronized (lock) {
            Set<Long> channelSubscribers = channels.getOrDefault(channel, Collections.emptySet());
            for (Long channelSubscriber : channelSubscribers) {
                sessions.addAll(webSocketSessions.getOrDefault(channelSubscriber, Collections.emptySet()));
            }
        }
        sendToTargetSessions(sessions, "msg,"+channel+","+content);
        return !sessions.isEmpty();
    }

    @Override
    public boolean sendToUserOnChannel(String channel, long userId, String content) {
        Set<WebSocketSession> sessions = new HashSet<>();
        synchronized (lock) {
            Set<Long> channelSubscribers = channels.getOrDefault(channel, Collections.emptySet());
            channelSubscribers.stream()
                    .filter(id -> userId == id)
                    .findFirst()
                    .ifPresent(id -> sessions.addAll(webSocketSessions.getOrDefault(id, Collections.emptySet())));
        }
        sendToTargetSessions(sessions, "msg,"+channel+","+content);
        return !sessions.isEmpty();
    }
}
