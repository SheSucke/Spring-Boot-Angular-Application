package eu.profinit.stm.mapperMyBatis.message;

import eu.profinit.stm.configuration.MyBatisConfiguration;
import eu.profinit.stm.mapperMyBatis.user.UserMapperMyBatis;
import eu.profinit.stm.model.event.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ContextConfiguration(classes = MyBatisConfiguration.class)
@TestPropertySource("/test.properties")
public class MessageMapperMyBatisTest {

    @Autowired
    private MessageMapperMyBatis messageMapperMyBatis;

    @Autowired
    private UserMapperMyBatis userMapperMyBatis;

    @Test
    public void insertMessage() {
        Long userId = 12L;
        Long eventId = 3L;
        Message message = new Message(userMapperMyBatis.findUserById(userId), "Insert message", LocalDateTime.now(), eventId);
        messageMapperMyBatis.insertMessage(message);
        Assert.assertNotNull(message.getEntityId());
        Assert.assertNotNull(messageMapperMyBatis.findMessageById(message.getEntityId()));
    }

    @Test
    public void deletePlaceById() {
        Long messageId = 1L;
        messageMapperMyBatis.deleteMessageById(messageId);
        Assert.assertNull(messageMapperMyBatis.findMessageById(messageId));
    }

    @Test
    public void findPlaceById() {
        Long messageId = 2L;
        Message message = messageMapperMyBatis.findMessageById(messageId);
        Assert.assertNotNull(message);
    }

    /**
    @Test
    public void updateMessage() {
        Long messageId = 3L;
        Message message = messageMapperMyBatis.findMessageById(messageId);
        String prevText = message.getText();
        message.setText("New text content");
        messageMapperMyBatis.updateMessage(message);
        Assert.assertNotEquals(prevText, message.getText());
    }
     */
}
