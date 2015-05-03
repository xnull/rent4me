package name.dargiri.web.stubs;

import bynull.realty.components.api.ChatMessageUsersOnlineNotifier;
import bynull.realty.dto.ChatMessageDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by dionis on 5/2/15.
 */
@Component
public class ChatMessageOnlineNotifierStub implements ChatMessageUsersOnlineNotifier {
    @Override
    public void sendMessagesToParticipants(ChatMessageDTO chatMessageDTO) {

    }
}
