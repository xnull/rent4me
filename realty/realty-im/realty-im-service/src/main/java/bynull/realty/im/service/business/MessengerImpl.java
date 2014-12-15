package bynull.realty.im.service.business;

import bynull.realty.im.dao.entity.ChatEntity;
import bynull.realty.im.dao.entity.MessageEntity;
import bynull.realty.im.dao.repo.MessageDao;
import bynull.realty.im.dao.repo.UserChatDao;
import bynull.realty.im.model.common.Timestamp;
import bynull.realty.im.model.ids.ChatId;
import bynull.realty.im.service.api.Messenger;
import bynull.realty.im.service.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 14.12.14.
 */
@Service
@Lazy
@Slf4j
public class MessengerImpl implements Messenger {
    @Autowired
    private UserChatDao userChatDao;
    @Autowired
    private MessageDao messageDao;

    @Override
    public void sendChatMessage(MessageDto message) {
        Integer senderId = message.getMessageId().getSenderId();

        ChatEntity userChat = userChatDao.getUserChat(senderId, message.getChatId());
        if (Objects.isNull(userChat)) {
            createUserChats(message.getChatId(), message.getMessageId().getSendingTimestamp());
        }

        //сохранить мессадж
        messageDao.saveMessage(new MessageEntity(message.getMessageId(), message.getText()));
    }

    @Override
    public void createChat(ChatId chatId) {
        Timestamp timestamp = new Timestamp();

        createUserChats(chatId, timestamp);
    }

    private void createUserChats(ChatId chatId, Timestamp timestamp) {
        ChatEntity firstUserChat = new ChatEntity(timestamp, chatId, timestamp.getTimestamp());
        userChatDao.saveUserChat(firstUserChat, chatId.getFirstUser());

        ChatEntity secondUserChat = new ChatEntity(timestamp, chatId, timestamp.getTimestamp());
        userChatDao.saveUserChat(secondUserChat, chatId.getSecondUser());
    }

    @Override
    public List<MessageDto> getAllMessages(ChatId chatId) {
        log.trace("Get all messages: {}", chatId);

        return messageDao.getAllMessages(chatId)
                .stream()
                .map(entity -> new MessageDto(entity.getMessageId(), entity.getText()))
                .collect(Collectors.toList());
    }
}
