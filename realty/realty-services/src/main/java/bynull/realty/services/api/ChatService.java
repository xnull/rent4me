package bynull.realty.services.api;

import bynull.realty.data.business.chat.ChatMessage;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.util.LimitAndOffset;

import java.util.List;

/**
 * Created by dionis on 06/01/15.
 */
public interface ChatService {
    ChatMessageDTO createChatMessage(long receiverId, String text);

    /**
     * Return grouped by chat key messages - unique per conversation.
     * @return
     */
    List<ChatMessageDTO> listMyLatestChatMessages();
    List<ChatMessageDTO> listMyLatestChatMessagesByKey(ChatMessageDTO.ChatKeyDTO chatKey, LimitAndOffset limitAndOffset);
}
