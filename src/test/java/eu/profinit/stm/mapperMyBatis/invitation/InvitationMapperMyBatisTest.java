package eu.profinit.stm.mapperMyBatis.invitation;

import eu.profinit.stm.configuration.MyBatisConfiguration;
import eu.profinit.stm.mapperMyBatis.user.UserMapperMyBatis;
import eu.profinit.stm.model.invitation.Invitation;
import eu.profinit.stm.model.invitation.StatusEnum;
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
public class InvitationMapperMyBatisTest {

    @Autowired
    private InvitationMapperMyBatis invitationMapperMyBatis;

    @Autowired
    private UserMapperMyBatis userMapperMyBatis;

    @Test
    public void insertInvitation() {
        Long userId = 13L;
        Long eventId = 4L;
        Invitation invitation = new Invitation(LocalDateTime.now(), null, StatusEnum.ACCEPTED, userMapperMyBatis.findUserById(userId), eventId);
        invitationMapperMyBatis.insertInvitation(invitation);
        Assert.assertNotNull(invitation.getEntityId());
        Assert.assertNotNull(invitationMapperMyBatis.findInvitationById(invitation.getEntityId()));
    }

    @Test
    public void deleteInvitationById() {
        Long invitationId = 1L;
        invitationMapperMyBatis.deleteInvitationById(invitationId);
        Assert.assertNull(invitationMapperMyBatis.findInvitationById(invitationId));
    }

    @Test
    public void findInvitationById() {
        Long invitationId = 2L;
        Invitation invitation = invitationMapperMyBatis.findInvitationById(invitationId);
        Assert.assertNotNull(invitation);
    }

     @Test
     public void updateInvitation() {
     Long invitationId = 3L;
     Invitation invitation = invitationMapperMyBatis.findInvitationById(invitationId);
     StatusEnum prevStatus = invitation.getStatus();
     invitation.setStatus(StatusEnum.PENDING);
     invitationMapperMyBatis.updateInvitation(invitation);
     Assert.assertNotEquals(prevStatus, invitation.getStatus());
     }

     @Test
    public void findInvitationByEventIdAndUserId() {
        Long eventId = 4L;
        Long userId = 17L;
        Invitation invitation = invitationMapperMyBatis.findInvitationByEventIdAndUserId(eventId, userId);
        Assert.assertNotNull(invitation);
     }
}
