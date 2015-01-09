package bynull.realty.im.dao.entity;

import bynull.realty.im.model.ids.ChatId;
import bynull.realty.im.model.ids.MessageId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Wither;

import java.io.Serializable;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageEntity implements Serializable {
    private final MessageId messageId;
    private final String text;
    /**
     * Время прочтения сообщения собеседником
     */
    private final Long readTimestamp;
    @Wither
    private final Long sendingTimestamp;

    public MessageEntity(MessageId messageId, String text) {
        this.messageId = messageId;
        this.text = text;
        this.readTimestamp = 0L;
        this.sendingTimestamp = messageId.getSendingTimestamp().getTimestamp();
    }

    public ChatId getChatId() {
        return messageId.getChatId();
    }

    public Integer getSenderId() {
        return messageId.getSenderId();
    }

    public Integer getReceiverId() {
        return messageId.getReceiverId();
    }
}
