package bynull.realty.im.dao.repo;

import bynull.realty.im.dao.entity.ChatEntity;
import bynull.realty.im.model.common.Timestamp;
import bynull.realty.im.model.ids.ChatId;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.IntStream;

/**
 * @author Vyacheslav Petc (v.pets@oorraa.net)
 * @since 13.12.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ImDaoTestConfig.class})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
public class UserChatDaoTest {

    @Autowired
    private UserChatDao userChatDao;

    /**
     * Создать чат
     */
    @Test
    //@Repeat(value = 10)
    public void saveChat() {
        long ts = System.currentTimeMillis();
        Timestamp creationTime = new Timestamp(ts);

        List<ChatEntity> testEntities = new ArrayList<>();

        int chatSize = 100;
        IntStream.rangeClosed(1, chatSize).forEach(i -> {
            ChatEntity chat = new ChatEntity(creationTime, new ChatId(1, i + 1), i * 10 + ts);
            testEntities.add(chat);
            userChatDao.saveUserChat(chat, 1);
        });

        SortedSet<ChatEntity> chats = userChatDao.getAllUserChats(1);

        //assertEquals(chatSize, chats.size());
        int randomChat = new Random().nextInt(chatSize);
        //assertEquals(testEntities.get(randomChat), chats.get(randomChat));

        Iterator<ChatEntity> it = chats.iterator();
        for (int i = 0; i < 15; i++) {
            System.out.println(it.next());
        }
    }
}
