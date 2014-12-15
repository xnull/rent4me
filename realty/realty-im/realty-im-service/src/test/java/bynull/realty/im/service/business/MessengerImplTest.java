package bynull.realty.im.service.business;

import bynull.realty.im.model.ids.MessageId;
import bynull.realty.im.service.ImApplicationConfig;
import bynull.realty.im.service.dto.MessageDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ImApplicationConfig.class})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MessengerImplTest {

    @Autowired
    private MessengerImpl messenger;

    @Test
    public void testSendChatMessage() throws Exception {
        MessageDto message = new MessageDto(MessageId.build(1, 2), "hey hello");
        messenger.sendChatMessage(message);

        List<MessageDto> messages = messenger.getAllMessages(message.getChatId());

        assertEquals(1, messages.size());
    }
}