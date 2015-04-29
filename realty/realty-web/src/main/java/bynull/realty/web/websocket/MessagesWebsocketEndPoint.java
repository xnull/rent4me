package bynull.realty.web.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by dionis on 4/29/15.
 */
@Component
public class MessagesWebsocketEndPoint extends TextWebSocketHandler {

    Set<WebSocketSession> webSocketSessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        webSocketSessions.add(session);
        System.out.println("Opened");
        TextMessage returnMessage = new TextMessage("User joned. Users online: "+webSocketSessions.size());
//        sendMessagesToAll(returnMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        webSocketSessions.remove(session);
        System.out.println("Closed");
        TextMessage returnMessage = new TextMessage("User left. Users online: "+webSocketSessions.size());
//        sendMessagesToAll(returnMessage);
    }



    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        webSocketSessions.remove(session);
        System.out.println("Transport error");
        TextMessage returnMessage = new TextMessage("User left. Users online: "+webSocketSessions.size());
//        sendMessagesToAll(returnMessage);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);

        TextMessage returnMessage = new TextMessage(message.getPayload()+"");
        sendMessagesToAll(returnMessage);
    }

    private void sendMessagesToAll(TextMessage returnMessage) throws IOException {
        for (WebSocketSession webSocketSession : webSocketSessions) {
            if(webSocketSession.isOpen()){
//                System.out.println("opened");
                webSocketSession.sendMessage(returnMessage);
            } else {
                System.out.println("closed. skipping");
            }
        }
    }
}
