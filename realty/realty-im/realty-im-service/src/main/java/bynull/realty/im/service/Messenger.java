package bynull.realty.im.service;

import bynull.realty.im.service.model.MessageDto;
import bynull.realty.im.service.model.ids.ChatId;

/**
 * Мессенджер, содержит всё необходимое апи для отправки сообщений между пользователями.
 *
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
public interface Messenger {
    void sendChatMessage(ChatId chatId, MessageDto message);
    void createChat(ChatId chatId);

}
