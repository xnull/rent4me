package bynull.realty.im.model.ids;

import bynull.realty.im.model.common.RandomGenerator;
import bynull.realty.im.model.common.Timestamp;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

/**
 * Индентификатор сообщения, определяется натуральным ключом: ид отправителя и ид получателя
 * по крайней мере для чатиков это так, для сделок наверное будет ключ с дополнительными полями
 *
 * @author Vyacheslav Petc
 * @since 13.12.14.
 */
@ToString
@Value
@EqualsAndHashCode
public class MessageId implements Serializable {
    private final Integer senderId;
    private final Integer receiverId;
    private final Timestamp sendingTimestamp;
    private final String hash;

    public static MessageId build(Integer senderId, Integer receiverId){
        return new MessageId(senderId, receiverId, new Timestamp(), RandomGenerator.randomString(3));
    }

    public ChatId getChatId(){
        return new ChatId(senderId, receiverId);
    }
}
