package bynull.realty.dao;

import bynull.realty.DbTest;
import bynull.realty.data.business.User;
import bynull.realty.data.business.chat.ChatMessage;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ChatMessageRepositoryTest extends DbTest {

    @Resource
    ChatMessageRepository repository;

    @Test
    public void createChatMessage() {
        User sender = createUser();
        User receiver = createUser();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(sender);
        chatMessage.setMessage("text");
        chatMessage.setReceiver(receiver);

        chatMessage = repository.saveAndFlush(chatMessage);

        assertThat(chatMessage.getId(), is(notNullValue()));
        assertThat(chatMessage.getChatKey(), is(notNullValue()));
        assertThat(chatMessage.getChatKey().getKey(), is(notNullValue()));
    }

    @Test
    public void listLatest() throws InterruptedException {
        User user1 = createUser();
        User user2 = createUser();

        ChatMessage first = new ChatMessage();
        first.setSender(user1);
        first.setMessage("text");
        first.setReceiver(user2);

        first = repository.saveAndFlush(first);

        Thread.sleep(100);

        ChatMessage second = new ChatMessage();
        second.setSender(user2);
        second.setMessage("text");
        second.setReceiver(user1);

        second = repository.saveAndFlush(second);

        List<ChatMessage> latestChatMessages = repository.findLatestChatMessages(ChatMessage.ChatKeyGenerator.getChatKey(user1, user2), new ChatMessageRepository.DescChatMessagePageRequest(0, 10));
        assertThat(latestChatMessages.size(), is(2));
        Iterator<ChatMessage> chatMessageIterator = latestChatMessages.iterator();

        assertThat(chatMessageIterator.next(), is(second));
        assertThat(chatMessageIterator.next(), is(first));
    }

    @Test
    public void findLatestChatMessagesOneConversation() throws InterruptedException {
        User user1 = createUser();
        User user2 = createUser();

        ChatMessage first = new ChatMessage();
        first.setSender(user1);
        first.setReceiver(user2);
        first.setMessage("text");

        first = repository.saveAndFlush(first);

        Thread.sleep(100);

        ChatMessage second = new ChatMessage();
        second.setSender(user2);
        second.setReceiver(user1);
        second.setMessage("text");

        second = repository.saveAndFlush(second);

        List<ChatMessage> latestChatMessages = repository.findLatestChatMessagesForUser(user1.getId());
        assertThat(latestChatMessages.size(), is(1));

        Iterator<ChatMessage> chatMessageIterator = latestChatMessages.iterator();
        assertThat(chatMessageIterator.next(), is(second));
    }

    @Test
    public void findLatestChatMessagesMultipleConversations() throws InterruptedException {
        User user1 = createUser();
        User user2 = createUser();
        User user3 = createUser();

        ChatMessage first = new ChatMessage();
        first.setSender(user1);
        first.setReceiver(user2);
        first.setMessage("text");

        first = repository.saveAndFlush(first);

        Thread.sleep(100);

        ChatMessage second = new ChatMessage();
        second.setSender(user2);
        second.setReceiver(user1);
        second.setMessage("text");

        second = repository.saveAndFlush(second);

        ChatMessage third = new ChatMessage();
        third.setSender(user3);
        third.setReceiver(user1);
        third.setMessage("text");

        third = repository.saveAndFlush(third);

        {
            List<ChatMessage> latestChatMessages = repository.findLatestChatMessagesForUser(user1.getId());
            assertThat(latestChatMessages.size(), is(2));

            Iterator<ChatMessage> chatMessageIterator = latestChatMessages.iterator();
            assertThat(chatMessageIterator.next(), is(third));
            assertThat(chatMessageIterator.next(), is(second));
        }

        {
            List<ChatMessage> latestChatMessages = repository.findLatestChatMessagesForUser(user2.getId());
            assertThat(latestChatMessages.size(), is(1));

            Iterator<ChatMessage> chatMessageIterator = latestChatMessages.iterator();
            assertThat(chatMessageIterator.next(), is(second));
        }

        {
            List<ChatMessage> latestChatMessages = repository.findLatestChatMessagesForUser(user3.getId());
            assertThat(latestChatMessages.size(), is(1));

            Iterator<ChatMessage> chatMessageIterator = latestChatMessages.iterator();
            assertThat(chatMessageIterator.next(), is(third));
        }
    }

}