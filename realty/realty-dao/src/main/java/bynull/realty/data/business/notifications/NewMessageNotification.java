package bynull.realty.data.business.notifications;

import bynull.realty.data.business.chat.ChatMessage;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by dionis on 5/2/15.
 */
@Entity
@DiscriminatorValue(Notification.Types.NEW_MESSAGE_STRING_VALUE)
public class NewMessageNotification extends Notification {
    @ManyToOne
    @JoinColumn(name = "chat_message_id")
    private ChatMessage chatMessage;

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    @Override
    public Type getType() {
        return Type.NEW_MESSAGE;
    }
}
