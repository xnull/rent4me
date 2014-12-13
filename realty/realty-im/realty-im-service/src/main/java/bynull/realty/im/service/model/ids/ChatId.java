package bynull.realty.im.service.model.ids;

import lombok.Value;

/**
 * Идентификатор разговора двух пользователей.
 *
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@Value
public class ChatId {
    private final Integer user1;
    private final Integer user2;
}
