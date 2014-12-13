package bynull.realty.im.service.model.ids;

import lombok.Value;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@Value
public class MessageId {
    private final Integer senderId;
    private final Integer receiverId;
}
