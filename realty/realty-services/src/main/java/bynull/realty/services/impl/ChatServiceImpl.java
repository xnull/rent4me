package bynull.realty.services.impl;

import bynull.realty.dao.ChatMessageRepository;
import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.chat.ChatMessage;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.services.api.ChatService;
import bynull.realty.util.LimitAndOffset;
import bynull.realty.utils.SecurityUtils;
import org.springframework.data.domain.PageRequest;
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

    @Transactional
    @Override
    public ChatMessageDTO createChatMessage(long receiverId, String text) {
        long sender = SecurityUtils.getAuthorizedUser().getId();

        if(sender == receiverId) {
            throw new BadRequestException("Sender and receiver could not be the same");
        }

        ChatMessage entity = new ChatMessage();
        entity.setSender(userRepository.findOne(sender));
        entity.setReceiver(userRepository.findOne(receiverId));
        entity = chatMessageRepository.saveAndFlush(entity);
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
