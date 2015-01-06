package bynull.realty.dao;

import bynull.realty.data.business.User;
import bynull.realty.data.business.chat.ChatMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by dionis on 06/01/15.
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{
    @Query("select msg from ChatMessage msg where msg.chatKey=:chatKey")
    List<ChatMessage> findLatestChatMessages(@Param("chatKey") ChatMessage.ChatKey chatKey, Pageable pageable);

    @Query(value =
            "select m.* from chat_messages m inner join ("+
            "SELECT distinct on(msg.chat_key) msg.id, msg.created_dt FROM chat_messages msg " +
            "WHERE msg.sender_id=:userId OR msg.receiver_id=:userId " +
                    "ORDER BY msg.chat_key, msg.created_dt DESC, msg.id " +
                    ") tmp on tmp.id=m.id order by m.created_dt DESC",
            nativeQuery = true)
    List<ChatMessage> findLatestChatMessagesForUser(@Param("userId") long userId);

    class DescChatMessagePageRequest extends PageRequest {
        public DescChatMessagePageRequest(int page, int size) {
            super(page, size, Sort.Direction.DESC, "created");
        }
    }
}
