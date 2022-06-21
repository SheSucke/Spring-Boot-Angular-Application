package eu.profinit.stm.mapperMyBatis.event;

import eu.profinit.stm.configuration.MyBatisConfiguration;
import eu.profinit.stm.mapperMyBatis.place.PlaceMapperMyBatis;
import eu.profinit.stm.mapperMyBatis.user.UserMapperMyBatis;
import eu.profinit.stm.model.event.Event;
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
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ContextConfiguration(classes = MyBatisConfiguration.class)
@TestPropertySource("/test.properties")
public class EventMapperMyBatisTest {

    @Autowired
    private EventMapperMyBatis eventMapperMyBatis;

    @Autowired
    private PlaceMapperMyBatis placeMapperMyBatis;

    @Autowired
    private UserMapperMyBatis userMapperMyBatis;


    @Test
    public void insertEvent() {
        Long userId = 11L;
        Long placeId = 4L;
        Event event = new Event(LocalDateTime.now(), 20, false,
                placeMapperMyBatis.findPlaceById(placeId), userMapperMyBatis.findUserById(userId),
                new ArrayList<>(), new ArrayList<>());
        eventMapperMyBatis.insertEvent(event);
        Assert.assertNotNull(event.getEntityId());
        Assert.assertNotNull(eventMapperMyBatis.findEventById(event.getEntityId()));
    }

    @Test
    public void deleteEventById() {
        Long eventId = 1L;
        eventMapperMyBatis.deleteEventById(eventId);
        Assert.assertNull(eventMapperMyBatis.findEventById(eventId));
    }

    @Test
    public void findEventById() {
        Long eventId = 2L;
        Event event = eventMapperMyBatis.findEventById(eventId);
        Assert.assertNotNull(event);
    }

    @Test
    public void updateEvent() {
        Long eventId = 2L;
        Event event = eventMapperMyBatis.findEventById(eventId);
        LocalDateTime prevTime = event.getDate();
        event.setDate(LocalDateTime.now());
        eventMapperMyBatis.updateEvent(event);
        Assert.assertNotEquals(prevTime, event.getDate());
    }
}
