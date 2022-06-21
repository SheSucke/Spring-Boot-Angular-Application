/*
 * StubInvitationService
 *
 * 0.1
 *
 * Author: M. Halamka
 */package eu.profinit.stm.service.invitation;

import eu.profinit.stm.crypto.Aes;
import eu.profinit.stm.dto.invitation.InvitationDto;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.exception.NonValidUriException;
import eu.profinit.stm.model.event.Event;
import eu.profinit.stm.model.event.Message;
import eu.profinit.stm.model.event.Place;
import eu.profinit.stm.model.invitation.Invitation;
import eu.profinit.stm.model.invitation.StatusEnum;
import eu.profinit.stm.model.user.Guest;
import eu.profinit.stm.model.user.User;
import eu.profinit.stm.model.user.UserParent;
import eu.profinit.stm.repository.invitation.InvitationRepository;
import eu.profinit.stm.service.event.EventService;
import eu.profinit.stm.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stub implementation of Invitation service interface. Includes all business logic related to invitations.
 */
@Service
public class InvitationServiceStub implements InvitationService {

    @Autowired
    InvitationRepository invitationRepository;

    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    Event event;
    User loggedUser;
    Guest guest;

    public InvitationServiceStub() {
        Place place = new Place("Profinit","Tychonova 2", 1L);
        loggedUser = new User("Ivan", "Stastny", "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "is@gmail.com");
        event = new Event(LocalDateTime.now(),6,false, place, loggedUser,new ArrayList<>(),new ArrayList<>());
        event.setEntityId(0L);
        event.getMessageList().add(new Message(loggedUser,"Testuji",LocalDateTime.now(), event.getEntityId()));
        guest = new Guest("Karel","mxPR4fbWzvai60UMLhD3aw==");
        guest.setEntityId(0L);
        event.getInvitationList().add(new Invitation(LocalDateTime.now(),LocalDateTime.now(), StatusEnum.PENDING,loggedUser, event.getEntityId()));
        event.getInvitationList().add(new Invitation(LocalDateTime.now(),LocalDateTime.now(), StatusEnum.PENDING,guest, event.getEntityId()));

    }

    /**
     * Creates now invitation and saves it to repository
     *
     * @param email email of user who gets invitation
     * @param eventId ID of event for which is new invitation is created
     * @return created invitation
     * @throws EntityNotFoundException if entity is not found.
     * @throws EntityAlreadyExistsException user was already invited to an Event
     */
    @Override
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
    @Override
    public Invitation changeInvitationStatus(Long eventId, String email, StatusEnum status) throws EntityNotFoundException {
        Invitation invitation = findInvitationByEventIdAndEmail(eventId,email);

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
    @Override
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
    @Override
    public List<Invitation> createNewInvitationsFromList(List<User> userList, Long eventId)
            throws EntityNotFoundException, EntityAlreadyExistsException {
        List<Invitation> invitationList = new ArrayList<>();
        for(User user: userList) {
            invitationList.add(createNewInvitation(user.getEmail(), eventId));
        }
        return invitationList;
    }

    /**
     * Filter invitation list by status and sort it according to "changed" date from oldest to newest
     *
     * @param invitationDtoList List that is going to be sorted
     * @param status according to which list will be filtered
     * @return Sorted and filtered list
     */
    @Override
    public List<InvitationDto> OrderListOfInvitationByDateForSpecificStatus(List<InvitationDto> invitationDtoList, StatusEnum status) {
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
     * Methods deletes user invitation for selected event
     *
     * @param email email of user whose invitation will be deleted
     * @param eventId ID of event on which invitation will be deleted
     * @throws EntityNotFoundException if entity was not found.
     */
    @Override
    public void deleteInvitation (String email, Long eventId) throws EntityNotFoundException {
        invitationRepository.deleteInvitation(email, eventId);
    }

    /**
     * Decrypt URI, parse and finds Guest Invitation in testing Event
     * @param uri identification of invitation
     * @return found invitation
     * @throws EntityNotFoundException thrown when Invitation, Event or Guest was not found
     * @throws NonValidUriException thrown when URI is invalid
     */
    @Override
    public Invitation getGuestInvitation(String uri) throws EntityNotFoundException, NonValidUriException {
        String decryptedUri = Aes.decrypt(uri);

        if (decryptedUri == null){
            throw new NonValidUriException();
        }

        Long eventId = Long.parseLong(decryptedUri.split("-")[1]);
        Event event;

        if (eventId.equals(this.event.getEntityId())){
            event = this.event;
        } else {
            throw new EntityNotFoundException("Event");

        }
        List <Invitation> invitationList = event.getInvitationList();
        Guest guest;

        if (uri.equals("mxPR4fbWzvai60UMLhD3aw==")) {
            guest = this.guest;
        } else if (uri.equals("jsem_place_holder")) {
            guest = this.guest;
        } else {
            throw new EntityNotFoundException("Guest");
        }

        for (Invitation invitation : invitationList){
            if (invitation.getRecipient().equals(guest)){
                return invitation;
            }
        }
        throw new EntityNotFoundException("Invitation");
    }

    /**
     * Finds event by id, creates new Guest and invitation
     * @param eventId id of event to which guest if invited
     * @param name name of guest
     * @return created invitation
     * @throws EntityNotFoundException thrown when Event entity is not found
     */
    @Override
    public Invitation createGuestInvitation(Long eventId, String name) throws EntityNotFoundException, EntityAlreadyExistsException {
        Event event = eventService.findEventById(eventId);
        Guest guest = userService.createNewGuest(name,event.getEntityId());
        Invitation invitation = new Invitation(LocalDateTime.now(), LocalDateTime.now(), StatusEnum.PENDING, guest, eventId);
        invitationRepository.insertInvitation(invitation);
        eventService.addNewInvitation(eventId, invitation);
        return  invitation;
    }

    /**
     * Finds invitation by uri and changes its status
     * @param uri identification od invitation
     * @param status status to which invitation should be changed
     * @return updated invitation
     * @throws NonValidUriException thrown when uri is not valid
     * @throws EntityNotFoundException thrown when entity is not found
     */
    @Override
    public Invitation changeGuestInvitation(String uri, StatusEnum status) throws NonValidUriException, EntityNotFoundException {
        Invitation invitation = getGuestInvitation(uri);
        invitation.setStatus(status);
        invitation.setChanged(LocalDateTime.now());
        invitationRepository.updateInvitation(invitation);
        return invitation;
    }
}
