/*
 * InvitationServiceImpl
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package eu.profinit.stm.service.invitation;

import eu.profinit.stm.crypto.Aes;
import eu.profinit.stm.dto.invitation.InvitationDto;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.exception.NonValidUriException;
import eu.profinit.stm.model.event.Event;
import eu.profinit.stm.model.invitation.Invitation;
import eu.profinit.stm.model.invitation.StatusEnum;
import eu.profinit.stm.model.user.Guest;
import eu.profinit.stm.model.user.User;
import eu.profinit.stm.model.user.UserParent;
import eu.profinit.stm.repository.invitation.InvitationRepository;
import eu.profinit.stm.service.event.EventService;
import eu.profinit.stm.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of Invitation service interface. Includes all business logic related to invitations.
 */
@Service
@AllArgsConstructor
@Profile("Main")
public class InvitationServiceImpl implements InvitationService {

    @Autowired
    InvitationRepository invitationRepository;
    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;

    /**
     * Creates now invitation and saves it to repository
     *
     * @param email email of user who gets invitation
     * @param eventId ID of event for which is new invitation is created
     * @return created invitation
     * @throws EntityNotFoundException if entity is not found.
     * @throws EntityNotFoundException user was already invited to an Event
     */
    public Invitation createNewInvitation(String email, Long eventId)
            throws EntityNotFoundException, EntityAlreadyExistsException {
        if (invitationRepository.isUserInvitedToEvent(email, eventId)) {
            throw new EntityAlreadyExistsException("User");
        }
        Invitation invitation = new Invitation(LocalDateTime.now(), LocalDateTime.now(), StatusEnum.PENDING, userService.findUserByEmail(email), eventId);
        invitationRepository.insertInvitation(invitation);
        eventService.addNewInvitation(eventId, invitation);
        return invitation;
    }

    /**
     * Changes StatusEnum of given invitation
     *
     * @param eventId ID of event
     * @param email email of user to which invitation belongs
     * @param status status into which will invitation be changed
     * @return Updated invitation
     * @throws EntityNotFoundException if entity is not found.
     */
    public Invitation changeInvitationStatus(Long eventId, String email, StatusEnum status) throws EntityNotFoundException {
        Invitation invitation = findInvitationByEventIdAndEmail(eventId, email);
        invitation.setStatus(status);
        invitation.setChanged(LocalDateTime.now());
        invitationRepository.updateInvitation(invitation);
        return invitation;
    }

    /**
     *  Finds Invitation for given user and from given event
     *
     * @param eventId ID of event
     * @param email email of user
     * @return Invitation for given user and from given event
     * @throws EntityNotFoundException if entity was not found.
     */
    public Invitation findInvitationByEventIdAndEmail(Long eventId,String email) throws EntityNotFoundException {
        Event event = eventService.findEventById(eventId);
        List <Invitation> invitationList = event.getInvitationList();
        UserParent user = userService.findUserByEmail(email);

        for (Invitation invitation : invitationList){
            if (invitation.getRecipient().equals(user)){
                return invitation;
            }
        }
        throw new EntityNotFoundException("Invitation");
    }

    /**
     * Creates invitations from user list
     *
     * @param userList Users who get invitation
     * @param eventId ID of event to which they will be invited
     * @return List of created invitations
     * @throws EntityNotFoundException if entity was not found.
     */
    public List<Invitation> createNewInvitationsFromList (List<User> userList, Long eventId)
            throws EntityNotFoundException, EntityAlreadyExistsException {
        List<Invitation> invitationList = new ArrayList<>();
        for(User user: userList){
            invitationList.add(createNewInvitation(user.getEmail(), eventId));
        }
        return invitationList;
    }

    /**
     * Methods deletes user invitation for selected event
     *
     * @param userEmail email of user whose invitation will be deleted
     * @param eventId ID of event on which invitation will be deleted
     * @throws EntityNotFoundException if entity was not found.
     */
    public void deleteInvitation (String userEmail, Long eventId) throws EntityNotFoundException {
        invitationRepository.deleteInvitation(userEmail, eventId);
    }

    /**
     * Filter invitation list by status and sort it according to "changed" date from oldest to newest
     *
     * @param invitationDtoList List that is going to be sorted
     * @param status according to which list will be filtered
     * @return Sorted and filtered list
     */
    public List<InvitationDto> OrderListOfInvitationByDateForSpecificStatus (List<InvitationDto> invitationDtoList, StatusEnum status){
        List<InvitationDto> result = new ArrayList<>();
        for (InvitationDto invitationDto : invitationDtoList){
            if(invitationDto.getStatus() == status){
                result.add(invitationDto);
            }
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Decrypts URI and finds Event and Guest from database, then finds Invitation from event and returns it.
     *
     * @param uri identification of guest invitation
     * @return Invitation that has ben found
     * @throws EntityNotFoundException thrown when Entity (Event, Invitation, Guest) is not found
     * @throws NonValidUriException thrown when URI is not valid AES cipher text.
     */
    public Invitation getGuestInvitation (String uri) throws EntityNotFoundException, NonValidUriException {
        String decryptedUri = Aes.decrypt(uri);
        if (decryptedUri == null){
            throw new NonValidUriException();
        }
        Long eventId = Long.parseLong(decryptedUri.split("-")[1]);//uri format is guestId + "-" + eventId
        Event event = eventService.findEventById(eventId);
        List <Invitation> invitationList = event.getInvitationList();
        Guest guest = userService.findGuestByUri(uri);

        for (Invitation invitation : invitationList){
            if (invitation.getRecipient().equals(guest)){
                return invitation;
            }
        }
        throw new EntityNotFoundException("Invitation");
    }

    /**
     * Creates new Guest and Invitation for given guest name and event
     * @param eventId id of event to which guest will be invited
     * @param name name of guest
     * @return Created Invitation
     * @throws EntityNotFoundException thrown when event entity is not found
     */
    public Invitation createGuestInvitation (Long eventId, String name) throws EntityNotFoundException, EntityAlreadyExistsException {
        Event event = eventService.findEventById(eventId);
        Guest guest = userService.createNewGuest(name, event.getEntityId());
        Invitation invitation = new Invitation(LocalDateTime.now(), LocalDateTime.now(), StatusEnum.PENDING, guest, eventId);
        invitationRepository.insertInvitation(invitation);
        eventService.addNewInvitation(eventId, invitation);
        return invitation;
    }

    /**
     * Finds Invitation from given URI and changes its status.
     * @param uri identification of invitation
     * @param status status to which invitation should be changed
     * @return Updated invitation
     * @throws NonValidUriException thrown when URI is not valid
     * @throws EntityNotFoundException thrown when invitation entity is not found.
     */
    public Invitation changeGuestInvitation (String uri, StatusEnum status) throws NonValidUriException, EntityNotFoundException {
        Invitation invitation = getGuestInvitation(uri);
        invitation.setStatus(status);
        invitation.setChanged(LocalDateTime.now());
        invitationRepository.updateInvitation(invitation);
        return invitation;
    }

}
