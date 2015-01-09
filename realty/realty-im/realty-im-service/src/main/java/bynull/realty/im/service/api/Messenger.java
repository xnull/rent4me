package bynull.realty.im.service.api;

import bynull.realty.im.model.ids.ChatId;
import bynull.realty.im.service.dto.MessageDto;

import java.util.List;

/**
 * Мессенджер, содержит всё необходимое апи для отправки сообщений между пользователями.
 *
 * @author Vyacheslav Petc
 * @since 13.12.14.
 */
public interface Messenger {
    void sendChatMessage(MessageDto message);

    void createChat(ChatId chatId);

    List<MessageDto> getAllMessages(ChatId chatId);
}
