package bynull.realty.im.dao.repo;

import bynull.realty.im.dao.entity.MessageEntity;
import bynull.realty.im.model.KeysUtil;
import bynull.realty.im.model.KeysUtil.ImMapKey;
import bynull.realty.im.model.ids.ChatId;
import bynull.realty.im.model.ids.MessageId;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Сохранение сообщения в бд.
 * Для сохранения и поиска нужно хранить сообщения в мапе, потом можно будет выбирать сообщения
 * по индексу с пагинацией и сортировать страницу списка на клиенте. В итоге получим плноценную пагинацию
 *
 * @author Vyacheslav Petc
 * @since 13.12.14.
 */
@Repository
@Lazy
@Slf4j
public class MessageDao extends AbstractHazelcastDao<MessageEntity> {

    private static final String SENDING_TS = "sendingTimestamp";

    private ImMapKey<MessageId, MessageEntity> getMessagesKey(ChatId chatId) {
        return ImMapKey.get(MessageId.class, MessageEntity.class,
                KeysUtil.IM_NS, KeysUtil.MESSAGE_NS, chatId.getFirstUser(), chatId.getSecondUser()
        );
    }

    /**
     * @param message
     */
    public void saveMessage(MessageEntity message) {
        log.debug("Save message: {}", message);

        IMap<MessageId, MessageEntity> storage = getStorage(message.getChatId());

        storage.addIndex(SENDING_TS, true);
        storage.put(message.getMessageId(), message);
    }

    private IMap<MessageId, MessageEntity> getStorage(ChatId chatId) {
        return getMap(getMessagesKey(chatId));
    }

    public SortedSet<MessageEntity> getAllMessages(ChatId chatId) {
        IMap<MessageId, MessageEntity> storage = getStorage(chatId);

        TreeSet<MessageEntity> sortedResult = new TreeSet<>(
                (o1, o2) -> o1.getSendingTimestamp().compareTo(o2.getSendingTimestamp())
        );

        sortedResult.addAll(
                storage.values(new SqlPredicate(SENDING_TS + " > 0")).stream().collect(Collectors.toList())
        );

        return sortedResult;
    }
}
