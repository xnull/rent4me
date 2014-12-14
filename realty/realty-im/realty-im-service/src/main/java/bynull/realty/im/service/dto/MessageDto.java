package bynull.realty.im.service.dto;

import bynull.realty.im.model.ids.ChatId;
import bynull.realty.im.model.ids.MessageId;
import lombok.Value;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@Value
public class MessageDto {
    private final MessageId messageId;
    private final String text;

    public ChatId getChatId(){
        return new ChatId(messageId.getSenderId(), messageId.getReceiverId());
    }
}
