package name.dargiri.web.stubs;

import bynull.realty.components.api.ChatMessageUsersOnlineNotifier;
import bynull.realty.dto.ChatMessageDTO;

import java.util.Collection;

/**
 * Created by dionis on 5/2/15.
 */
public class ChatMessageOnlineNotifierStub implements ChatMessageUsersOnlineNotifier {
    @Override
    public void sendMessagesToParticipants(ChatMessageDTO chatMessageDTO) {

    }

    @Override
    public Collection<Long> getUserIdsOnline() {
        return null;
    }

    @Override
    public boolean isUserOnline(long userId) {
        return false;
    }

    @Override
    public boolean sendToUserIfOnline(long userId, ChatMessageDTO content) {
        return false;
    }

    @Override
    public boolean broadcastToChannel(String channel, ChatMessageDTO content) {
        return false;
    }

    @Override
    public boolean sendToUserOnChannel(String channel, long userId, ChatMessageDTO content) {
        return false;
    }
}
