package eu.profinit.stm.repository.invitation;

import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.mapperMyBatis.invitation.InvitationMapperMyBatis;
import eu.profinit.stm.model.invitation.Invitation;
import eu.profinit.stm.repository.event.EventRepository;
import eu.profinit.stm.repository.user.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@Profile("Main")
public class InvitationRepositoryImpl implements InvitationRepository {

    @Autowired
    private InvitationMapperMyBatis invitationMapperMyBatis;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    private static final String EX_MSG = "Invitation";

    @Override
    public void insertInvitation(@NonNull Invitation invitation) throws EntityNotFoundException, EntityAlreadyExistsException {
        if (invitationMapperMyBatis.findInvitationById(invitation.getEntityId()) != null) {
            throw new EntityAlreadyExistsException(EX_MSG);
        }
        userRepository.findUserById(invitation.getRecipient().getEntityId());
        eventRepository.findEventById(invitation.getEventId());
        invitationMapperMyBatis.insertInvitation(invitation);
    }

    @Override
    public void updateInvitation(@NonNull Invitation invitation) throws EntityNotFoundException {
        if (invitationMapperMyBatis.findInvitationById(invitation.getEntityId()) == null) {
            throw new EntityNotFoundException(EX_MSG);
        }
        invitation.setChanged(LocalDateTime.now());
        invitationMapperMyBatis.updateInvitation(invitation);
    }

    @Override
    public Invitation findInvitationById(Long id) throws EntityNotFoundException {
        Invitation foundInvitation = invitationMapperMyBatis.findInvitationById(id);
        if (foundInvitation == null) {
            throw new EntityNotFoundException(EX_MSG);
        }
        return foundInvitation;
    }

    @Override
    public Invitation findInvitationByEventIdAndUserEmail(Long eventId, String userEmail) throws EntityNotFoundException {
        Invitation foundInvitation = invitationMapperMyBatis.findInvitationByEventIdAndUserId(eventId, userRepository.findUserByEmail(userEmail).getEntityId());
        if (foundInvitation == null) {
            throw new EntityNotFoundException(EX_MSG);
        }
        return foundInvitation;
    }

    @Override
    public void deleteInvitation(String userEmail, Long eventId) throws EntityNotFoundException {
        invitationMapperMyBatis.deleteInvitationById(findInvitationByEventIdAndUserEmail(eventId, userEmail).getEntityId());
    }

    @Override
    public boolean isUserInvitedToEvent(String userEmail, Long eventId) throws EntityNotFoundException {
        return invitationMapperMyBatis.findInvitationByEventIdAndUserId(eventId, userRepository.findUserByEmail(userEmail).getEntityId()) != null;
    }
}
