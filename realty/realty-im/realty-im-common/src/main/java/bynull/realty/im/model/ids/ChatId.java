package bynull.realty.im.model.ids;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Идентификатор разговора двух пользователей.
 *
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@Getter
@ToString
@EqualsAndHashCode
public class ChatId implements Serializable {
    private final Integer firstUser;
    private final Integer secondUser;

    public ChatId(Integer firstUser, Integer secondUser) {
        check(firstUser, secondUser);
        this.firstUser = Math.min(firstUser, secondUser);
        this.secondUser = Math.max(firstUser, secondUser);
    }

    /**
     * Участники диалога должны быть заданы и не должны совпадать
     */
    private void check(Integer user1, Integer user2) {
        if (user1 == null || user2 == null || user1.equals(user2)){
            throw new IllegalArgumentException("Invalid chat id. First user: " + this.firstUser + ", second user: " + this.secondUser);
        }
    }
}
