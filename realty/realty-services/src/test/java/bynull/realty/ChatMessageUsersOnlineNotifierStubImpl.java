package bynull.realty;

import bynull.realty.components.api.ChatMessageUsersOnlineNotifier;
import bynull.realty.dto.ChatMessageDTO;
import org.springframework.stereotype.Component;

/**
 * Created by dionis on 01/06/15.
 */
@Component
public class ChatMessageUsersOnlineNotifierStubImpl implements ChatMessageUsersOnlineNotifier {
    @Override
    public void sendMessagesToParticipants(ChatMessageDTO chatMessageDTO) {

    }
}
