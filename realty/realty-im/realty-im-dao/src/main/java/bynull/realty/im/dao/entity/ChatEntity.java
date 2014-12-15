package bynull.realty.im.dao.entity;

import bynull.realty.im.model.common.Timestamp;
import bynull.realty.im.model.ids.ChatId;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;

import java.io.Serializable;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@Value
@AllArgsConstructor
public class ChatEntity implements Serializable {
    private final Timestamp creationTime;
    private final ChatId chatId;
    @Wither
    private final Long lastUpdateTs;
}
