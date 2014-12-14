package bynull.realty.im.dao.repo;

import bynull.realty.im.dao.entity.ChatEntity;
import bynull.realty.im.model.KeysUtil;
import bynull.realty.im.model.KeysUtil.ImMapKey;
import bynull.realty.im.model.ids.ChatId;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@Repository
@Lazy
@Slf4j
public class UserChatDao extends AbstractHazelcastDao<ChatEntity> {
    /**
     * Индекс по времени последнего непрочитанного сообщения
     * Равно имени поля сущности по которому будет построен индекс
     */
    public static final String LAT_UPDATE_TS_INDEX = "lastUpdateTs";

    /**
     * Ключ по которому хранятся все чаты пользователя
     */
    private ImMapKey<ChatId, ChatEntity> getUserChatKey(Integer userId) {
        return ImMapKey.get(ChatId.class, ChatEntity.class, KeysUtil.IM_NS, KeysUtil.CHAT_NS, userId);
    }

    public void saveUserChat(ChatEntity chat, Integer userId) {
        log.debug("Save or update user chat: {}, user: {}", chat, userId);

        IMap<ChatId, ChatEntity> userChats = getUserChats(userId);
        userChats.addIndex(LAT_UPDATE_TS_INDEX, true);
        userChats.put(chat.getChatId(), chat);
    }

    public SortedSet<ChatEntity> getAllUserChats(Integer userId) {
        log.trace("Get all user chats: {}", userId);

        IMap<ChatId, ChatEntity> userChats = getUserChats(userId);
        SqlPredicate predicate = new SqlPredicate(LAT_UPDATE_TS_INDEX + "> 0 ");

        TreeSet<ChatEntity> sortedResult = new TreeSet<>(
                (o1, o2) -> o1.getLastUpdateTs().compareTo(o2.getLastUpdateTs())
        );

        sortedResult.addAll(userChats.values(predicate).stream().collect(Collectors.toList()));

        return sortedResult;
    }

    public ChatEntity getUserChat(Integer userId, ChatId chatId){
        return getUserChats(userId).get(chatId);
    }

    private IMap<ChatId, ChatEntity> getUserChats(Integer userId) {
        return getMap(getUserChatKey(userId));
    }
}
