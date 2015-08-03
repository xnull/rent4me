package bynull.realty.services.impl;

import bynull.realty.components.AfterCommitExecutor;
import bynull.realty.components.api.NotificationUsersOnlineNotifier;
import bynull.realty.converters.NotificationModelDTOConverter;
import bynull.realty.dao.ChatMessageRepository;
import bynull.realty.dao.NotificationRepository;
import bynull.realty.dao.UserRepository;
import bynull.realty.data.business.User;
import bynull.realty.data.business.chat.ChatMessage;
import bynull.realty.data.business.notifications.NewMessageNotification;
import bynull.realty.data.business.notifications.Notification;
import bynull.realty.dto.ChatMessageDTO;
import bynull.realty.dto.NotificationDTO;
import bynull.realty.services.api.NotificationService;
import bynull.realty.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by dionis on 5/2/15.
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    @Resource
    ChatMessageRepository chatMessageRepository;

    @Resource
    NotificationRepository notificationRepository;

    @Resource
    AfterCommitExecutor afterCommitExecutor;

    @Resource
    NotificationUsersOnlineNotifier notificationUsersOnlineNotifier;

    @Resource
    NotificationModelDTOConverter notificationModelDTOConverter;

    @Resource
    UserRepository userRepository;

    @Transactional
    @Override
    public void createNewChatMessageNotification(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = chatMessageRepository.findOne(chatMessageDTO.getId());

        NewMessageNotification notification = new NewMessageNotification();
        notification.setChatMessage(chatMessage);
        notification.setSender(chatMessage.getSender());
        notification.setReceiver(chatMessage.getReceiver());

        notification = notificationRepository.saveAndFlush(notification);
        long notificationId = notification.getId();

        afterCommitExecutor.executeAsynchronouslyInTransaction(() -> {
            notificationModelDTOConverter
                    .toTargetType(Optional.ofNullable(notificationRepository.findOne(notificationId)))
                    .ifPresent(notificationUsersOnlineNotifier::deliverNotification);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public List<? extends NotificationDTO> listMyUnreadNotifications() {
        long id = SecurityUtils.getAuthorizedUser().getId();
        User user = userRepository.findOne(id);

        return notificationModelDTOConverter.toTargetList(notificationRepository.listMyUnreadNotifications(user));
    }

    @Transactional
    @Override
    public void resolveMyNotifications(Set<Long> notificationIds) {
        Assert.notNull(notificationIds);
        if (notificationIds.isEmpty()) {
            return;
        }
        long id = SecurityUtils.getAuthorizedUser().getId();
        User user = userRepository.findOne(id);
        notificationRepository.resolveMyNotifications(user, notificationIds);
    }
}
