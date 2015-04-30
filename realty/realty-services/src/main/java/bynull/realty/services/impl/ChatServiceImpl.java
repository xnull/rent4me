package bynull.realty.services.impl;

import bynull.realty.components.AfterCommitExecutor;
import bynull.realty.components.api.ChatMessageUsersOnlineNotifier;
import bynull.realty.dao.ChatMessageRepository;
import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.User;
import bynull.realty.data.business.chat.ChatMessage;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.services.api.AsyncExecutor;
import bynull.realty.services.api.ChatService;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dionis on 06/01/15.
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private ChatMessageRepository chatMessageRepository;
    @Resource
    private UserRepository userRepository;

    @Resource
    ChatMessageUsersOnlineNotifier chatMessageUsersOnlineNotifier;

    @Resource
    AfterCommitExecutor afterCommitExecutor;

    @Transactional
    @Override
    public ChatMessageDTO createChatMessage(long receiverId, String text) {
        long senderId = SecurityUtils.getAuthorizedUser().getId();

        if (senderId == receiverId) {
            throw new BadRequestException("Sender and receiver could not be the same");
        }

        User sender = userRepository.findOne(senderId);
        Assert.notNull(sender);
        User receiver = userRepository.findOne(receiverId);
        Assert.notNull(receiver);

        ChatMessage entity = new ChatMessage();
        entity.setSender(sender);
        entity.setReceiver(receiver);
        entity.setMessage(text);
        entity = chatMessageRepository.saveAndFlush(entity);

        final long chatMessageId = entity.getId();

        afterCommitExecutor.executeAsynchronouslyInTransaction(()->{
            ChatMessage one = chatMessageRepository.findOne(chatMessageId);
            Assert.notNull(one, "Chat message not found by id "+chatMessageId);
            ChatMessageDTO dto = ChatMessageDTO.from(one);
            chatMessageUsersOnlineNotifier.sendMessagesToParticipants(dto);
        });

        return ChatMessageDTO.from(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatMessageDTO> listMyLatestChatMessages() {
        return chatMessageRepository.findLatestChatMessagesForUser(SecurityUtils.getAuthorizedUser().getId())
                .stream()
                .map(ChatMessageDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatMessageDTO> listMyLatestChatMessagesByKey(ChatMessageDTO.ChatKeyDTO chatKey, LimitAndOffset limitAndOffset) {
        Assert.notNull(chatKey);
        Assert.notNull(limitAndOffset);

        return chatMessageRepository.findLatestChatMessages(chatKey.toInternal(), limitAndOffset.toPageable(ChatMessageRepository.DescChatMessagePageRequest.class))
                .stream()
                .map(ChatMessageDTO::from)
                .collect(Collectors.toList());
    }
}
