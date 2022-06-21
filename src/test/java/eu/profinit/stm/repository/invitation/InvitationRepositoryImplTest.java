package eu.profinit.stm.repository.invitation;

import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.mapperMyBatis.invitation.InvitationMapperMyBatis;
import eu.profinit.stm.model.event.Event;
import eu.profinit.stm.model.invitation.Invitation;
import eu.profinit.stm.repository.event.EventRepositoryImpl;
import eu.profinit.stm.repository.user.UserRepositoryImpl;
import eu.profinit.stm.model.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

/**
 * Test class for invitation repository, mocked necessary classes
 */
@RunWith(MockitoJUnitRunner.class)
public class InvitationRepositoryImplTest {

    @InjectMocks
    private InvitationRepositoryImpl invitationRepository;

    @Mock
    private UserRepositoryImpl userRepository;

    @Mock
    private EventRepositoryImpl eventRepository;

    @Mock
    private InvitationMapperMyBatis invitationMapperMyBatis;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void insertNewSubgroup() throws EntityAlreadyExistsException, EntityNotFoundException {
        Invitation invitation = new Invitation();
        invitation.setRecipient(new User());
        when(invitationMapperMyBatis.findInvitationById(invitation.getEntityId())).thenReturn(null);
        invitationRepository.insertInvitation(invitation);
        verify(invitationMapperMyBatis, times(1)).findInvitationById(invitation.getEntityId());
        verify(invitationMapperMyBatis, times(1)).insertInvitation(invitation);
    }

    @Test
    public void insertInvitationThatAlreadyExists() {
        Invitation invitation = new Invitation();
        when(invitationMapperMyBatis.findInvitationById(invitation.getEntityId())).thenReturn(invitation);
        Exception ex = Assert.assertThrows(EntityAlreadyExistsException.class, () -> invitationRepository.insertInvitation(invitation));
        Assert.assertEquals(new EntityAlreadyExistsException("Invitation").getMessage(), ex.getMessage());
        verify(invitationMapperMyBatis, times(1)).findInvitationById(invitation.getEntityId());
        verify(invitationMapperMyBatis, times(0)).insertInvitation(invitation);
    }

    @Test
    public void updateInvitation() throws EntityNotFoundException {
        Invitation invitation = new Invitation();
        when(invitationMapperMyBatis.findInvitationById(invitation.getEntityId())).thenReturn(invitation);
        invitationRepository.updateInvitation(invitation);
        verify(invitationMapperMyBatis, times(1)).findInvitationById(invitation.getEntityId());
        verify(invitationMapperMyBatis, times(1)).updateInvitation(invitation);
    }

    @Test
    public void updateInvitationThatDoesNotExist() {
        Invitation invitation = new Invitation();
        when(invitationMapperMyBatis.findInvitationById(invitation.getEntityId())).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> invitationRepository.updateInvitation(invitation));
        Assert.assertEquals(new EntityNotFoundException("Invitation").getMessage(), ex.getMessage());
        verify(invitationMapperMyBatis, times(1)).findInvitationById(invitation.getEntityId());
        verify(invitationMapperMyBatis, times(0)).updateInvitation(invitation);
    }

    @Test
    public void deleteInvitation() throws EntityNotFoundException {
        Invitation invitation = new Invitation();
        Event event = new Event();
        User regUs = new User();
        when(userRepository.findUserByEmail(regUs.getEmail())).thenReturn(regUs);
        when(invitationMapperMyBatis.findInvitationByEventIdAndUserId(event.getEntityId(), regUs.getEntityId())).thenReturn(invitation);
        invitationRepository.deleteInvitation(regUs.getName(), event.getEntityId());
        verify(userRepository, times(1)).findUserByEmail(regUs.getEmail());
        verify(invitationMapperMyBatis, times(1)).findInvitationByEventIdAndUserId(event.getEntityId(), regUs.getEntityId());
        verify(invitationMapperMyBatis, times(1)).deleteInvitationById(invitation.getEntityId());
    }

    @Test
    public void deleteInvitationThatDoesNotExist() throws EntityNotFoundException {
        Invitation invitation = new Invitation();
        Event event = new Event();
        User regUs = new User();
        when(userRepository.findUserByEmail(regUs.getEmail())).thenReturn(regUs);
        when(invitationMapperMyBatis.findInvitationByEventIdAndUserId(event.getEntityId(), regUs.getEntityId())).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> invitationRepository.deleteInvitation(regUs.getName(), event.getEntityId()));
        Assert.assertEquals(new EntityNotFoundException("Invitation").getMessage(), ex.getMessage());
        verify(userRepository, times(1)).findUserByEmail(regUs.getEmail());
        verify(invitationMapperMyBatis, times(1)).findInvitationByEventIdAndUserId(event.getEntityId(), regUs.getEntityId());
        verify(invitationMapperMyBatis, times(0)).deleteInvitationById(invitation.getEntityId());
    }

    @Test
    public void findInvitationById() throws EntityNotFoundException {
        Invitation invitation = new Invitation();
        when(invitationMapperMyBatis.findInvitationById(invitation.getEntityId())).thenReturn(invitation);
        invitationRepository.findInvitationById(invitation.getEntityId());
        verify(invitationMapperMyBatis, times(1)).findInvitationById(invitation.getEntityId());
    }

    @Test
    public void findInvitationThatDoesNotExistById() {
        Invitation invitation = new Invitation();
        when(invitationMapperMyBatis.findInvitationById(invitation.getEntityId())).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> invitationRepository.findInvitationById(invitation.getEntityId()));
        Assert.assertEquals(new EntityNotFoundException("Invitation").getMessage(), ex.getMessage());
        verify(invitationMapperMyBatis, times(1)).findInvitationById(invitation.getEntityId());
    }

    @Test
    public void findInvitationByEventIdAndUserEmail() throws EntityNotFoundException {
        Invitation invitation = new Invitation();
        Event event = new Event();
        User regUs = new User();
        when(userRepository.findUserByEmail(regUs.getEmail())).thenReturn(regUs);
        when(invitationMapperMyBatis.findInvitationByEventIdAndUserId(event.getEntityId(), regUs.getEntityId())).thenReturn(invitation);
        invitationRepository.findInvitationByEventIdAndUserEmail(event.getEntityId(), regUs.getEmail());
        verify(userRepository, times(1)).findUserByEmail(regUs.getEmail());
        verify(invitationMapperMyBatis, times(1)).findInvitationByEventIdAndUserId(event.getEntityId(), regUs.getEntityId());
    }

    @Test
    public void findInvitationThatDoesNotExistByEventIdAndUserEmail() throws EntityNotFoundException {
        Event event = new Event();
        User regUs = new User();
        when(userRepository.findUserByEmail(regUs.getEmail())).thenReturn(regUs);
        when(invitationMapperMyBatis.findInvitationByEventIdAndUserId(event.getEntityId(), regUs.getEntityId())).thenReturn(null);
        Exception ex = Assert.assertThrows(EntityNotFoundException.class, () -> invitationRepository.findInvitationByEventIdAndUserEmail(event.getEntityId(), regUs.getEmail()));
        Assert.assertEquals(new EntityNotFoundException("Invitation").getMessage(), ex.getMessage());
        verify(userRepository, times(1)).findUserByEmail(regUs.getEmail());
        verify(invitationMapperMyBatis, times(1)).findInvitationByEventIdAndUserId(event.getEntityId(), regUs.getEntityId());
    }
}
