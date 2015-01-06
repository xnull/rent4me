package bynull.realty.dto;

import bynull.realty.data.business.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static bynull.realty.util.CommonUtils.copy;

/**
 * Created by dionis on 06/01/15.
 */

@Getter
@Setter
public class ChatMessageDTO {
    private Long id;
    private String message;
    private UserDTO sender;
    private UserDTO receiver;
    private Date created;
    private ChatKeyDTO chatKey;

    public Date getCreated() {
        return copy(created);
    }

    public void setCreated(Date created) {
        this.created = copy(created);
    }

    public static ChatMessageDTO from(ChatMessage message) {
        if (message == null) {
            return null;
        }
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(message.getId());
        dto.setSender(UserDTO.from(message.getSender()));
        dto.setReceiver(UserDTO.from(message.getReceiver()));
        dto.setMessage(message.getMessage());
        dto.setCreated(message.getCreated());
        dto.setChatKey(ChatKeyDTO.from(message.getChatKey()));
        return dto;
    }

    @Getter
    @AllArgsConstructor
    public static class ChatKeyDTO {
        private final String key;

        public static ChatKeyDTO from(ChatMessage.ChatKey chatKey) {
            if (chatKey == null) {
                return null;
            }
            return new ChatKeyDTO(chatKey.getKey());
        }

        public ChatMessage.ChatKey toInternal() {
            return new ChatMessage.ChatKey(getKey());
        }
    }
}
