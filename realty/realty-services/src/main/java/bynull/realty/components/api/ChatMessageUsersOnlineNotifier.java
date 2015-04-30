package bynull.realty.components.api;

import bynull.realty.dto.ChatMessageDTO;

/**
 * Created by dionis on 4/30/15.
 */
public interface ChatMessageUsersOnlineNotifier extends UsersOnlineNotifier<ChatMessageDTO> {
    void sendMessagesToParticipants(ChatMessageDTO chatMessageDTO);
}
