/*
 * EventController
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package eu.profinit.stm.controller.event;


import eu.profinit.stm.dto.event.EventDto;
import eu.profinit.stm.dto.event.MessageDto;
import eu.profinit.stm.dto.invitation.InvitationDto;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.exception.NonValidUriException;
import eu.profinit.stm.mapper.EventMapper;
import eu.profinit.stm.mapper.InvitationMapper;
import eu.profinit.stm.mapper.MessageMapper;
import eu.profinit.stm.model.event.Event;
import eu.profinit.stm.model.invitation.StatusEnum;
import eu.profinit.stm.model.user.Guest;
import eu.profinit.stm.service.event.EventService;
import eu.profinit.stm.service.invitation.InvitationService;
import eu.profinit.stm.service.user.AuthenticationFacade;
import eu.profinit.stm.service.user.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controller for event business logic
 */
@RestController
@Profile({"Main", "test"})
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    InvitationService invitationService;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    /**
     * Returns Event found by id or HttpStatus.NOT_FOUND (404)
     *
     * @param eventId ID of wanted event
     * @return EventDto of desired event or HttpStatus.NOT_FOUND if event was not found
     */
    @GetMapping (value = "/event/{eventId}")
    public @ResponseBody
    EventDto findEventById(@PathVariable Long eventId) {
        try {
            Event event = eventService.findEventById(eventId);
            return EventMapper.toDto(event);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
             throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
        }
    }

    /**
     * Post that creates new event in database
     *
     * @param eventDto from which is new Event created
     * @return EventDto of created Event,
     */
    @PostMapping (value = "/event")
    @ResponseStatus (HttpStatus.CREATED)
    public EventDto createNewEvent (@RequestBody EventDto eventDto) throws EntityAlreadyExistsException {
        return EventMapper.toDto(eventService.createNewEvent(eventDto));
    }

    /**
     * Post for updating event which is already in database. Returns NotFound (404) if event is not found
     *
     * @param eventId ID of wanted event
     * @param eventDto from which event will be updated
     * @return EvenDto of updated Event or HttpStatus.NOT_FOUND if Event was not found
     */
    @PostMapping(value = "/event/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto updateEvent (@PathVariable Long eventId, @RequestBody EventDto eventDto){
        try {
            return EventMapper.toDto(eventService.updateEvent(eventDto,eventId));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
        }
    }

    /**
     * Changes event status.
     *
     * @param eventId ID of event which status is going to be changed
     * @return EventDto of Event with changed status or HttpStatus.NOT_FOUND if Event was not found
     */
    @PostMapping(value = "/event/{eventId}/status")
    @ResponseStatus(HttpStatus.OK)
    public EventDto changeEventStatus (@PathVariable Long eventId){
        try {
            return EventMapper.toDto(eventService.changeEventStatus(eventId));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
        }
    }

    /**
     * Post new message to the event
     *
     * @param eventId ID of event where message should be sent
     * @param message which is going to be stored
     * @return MessageDto of created Message or HttpStatus.NOT_FOUND if Event was not found
     */
    @PostMapping(value = "/event/{eventId}/message/{message}")
    @ResponseStatus(HttpStatus.OK)
    public MessageDto addNewMessage (@PathVariable Long eventId, @PathVariable String message){
        try {
            Authentication authentication = authenticationFacade.getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            return MessageMapper.toDto(eventService.addNewMessage(userDetails.getUsername(), message,eventId));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
        }
    }

    /**
     * Gets list of all messages from given Event
     *
     * @param eventId ID of event from which messages should be extracted
     * @return List of MessageDto from Event or HttpStatus.NOT_FOUND if Event was not found
     */
    @GetMapping(value = "/event/{eventId}/messages")
    public @ResponseBody List<MessageDto> getAllMessages(@PathVariable Long eventId){
        try {
            return MessageMapper.toListOfDto(eventService.getAllMessages(eventId));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
        }
    }

    /**
     * Post new invitation to an Event
     *
     * @param eventId ID of event for which is invitation created
     * @param email Email of person who is invited
     * @return InvitationDto of Invitation that has been created or
     * HttpStatus.NOT_FOUND if Event or User was not found or
     * HttpStatus.CONFLICT if User has been already invited for given Event or
     * HttpStatus.BAD_REQUEST if something unexpected happens
     */
    @PostMapping(value = "/event/{eventId}/invitation/{email}")
    @ResponseStatus (HttpStatus.OK)
    public InvitationDto addNewInvitation(@PathVariable Long eventId, @PathVariable String email){
        try {
           return InvitationMapper.toDto(invitationService.createNewInvitation(email,eventId));
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            e.printStackTrace();
            if (e.getClass().equals(EntityNotFoundException.class)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            } else if (e.getClass().equals(EntityAlreadyExistsException.class)){
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns List of invitations for given Event.
     *
     * @param eventId ID of selected Event
     * @return List of invitations or HttpStatus.NOT_FOUND if Event was not found
     */
    @GetMapping (value = "/event/{eventId}/invitations")
    public @ResponseBody List<InvitationDto> getAllInvitations (@PathVariable Long eventId){
        try {
            return InvitationMapper.toDtoList(eventService.getAllInvitations(eventId));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
        }
    }

    /**
     * Changes Invitation StatusEnum for current user and given event
     *
     * @param eventId ID of selected Event
     * @param statusString Status to which should be invitation changed
     * @return DTO of changed Invitation or HttpStatus.NOT_FOUND if event was not found
     * or HttpStatus.BAD_REQUEST if statusString was in bad format or contained non-existing enum value
     */
    @PostMapping (value = "/event/{eventId}/invitation/statusChange/{statusString}")
    public InvitationDto changeInvitationStatus (@PathVariable Long eventId, @PathVariable String statusString){
        Authentication authentication = authenticationFacade.getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        try {
            StatusEnum status = StatusEnum.valueOf(statusString);
            return InvitationMapper.toDto(invitationService.changeInvitationStatus(eventId, userDetails.getUsername(),status));
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
            if (e.getClass().equals(EntityNotFoundException.class)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
            }
        }
    }

    /**
     * Returns sorted list of invitations for selected Event and Invitation status.
     *
     * @param eventId  ID of selected Event
     * @param statusString Status for which will invitations be sorted
     * @return Sorted list of invitations for given status or HttpStatus.NOT_FOUND if event was not found
     * or HttpStatus.BAD_REQUEST if statusString was in bad format or contained non-existing enum value
     */
    @GetMapping (value = "/event/{eventId}/invitations/{statusString}/sorted")
    public @ResponseBody List<InvitationDto> getSortedInvitations (@PathVariable Long eventId,@PathVariable String statusString){
        try {
            StatusEnum status = StatusEnum.valueOf(statusString);
            return invitationService.OrderListOfInvitationByDateForSpecificStatus(InvitationMapper.toDtoList(eventService.getAllInvitations(eventId)), status);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
            if (e.getClass().equals(EntityNotFoundException.class)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
            }
        }
    }

    /**
     * Calls invitationService that creates new guest invitation. And handles responses.
     *
     * @param name Name of guest
     * @param eventId ID of Event entity for which invitation should be created
     * @return URI of a new invitation
     */
    @PostMapping("/event/{eventId}/invitation/guest/{name}")
    public String createNewGuest (@PathVariable String name, @PathVariable Long eventId){
        try {
            return ((Guest) invitationService.createGuestInvitation(eventId,name).getRecipient()).getUri();
        } catch (EntityNotFoundException | EntityAlreadyExistsException e) {
            e.printStackTrace();
            if (e.getClass().equals(EntityNotFoundException.class))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
            }
        }
    }

    /**
     * Calls invitationService that find Invitation for given URI
     *
     * @param uri identification of Guest Invitation
     * @return InvitationDto of desired event
     */
    @GetMapping("/guest/invitation/{uri}")
    public InvitationDto getGuestInvitation (@PathVariable String uri) {
        try {
            return InvitationMapper.toDto(invitationService.getGuestInvitation(uri));
        } catch (EntityNotFoundException | NonValidUriException e) {
            e.printStackTrace();
            if (e.getClass().equals(EntityNotFoundException.class))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
            }
        }
    }

    /**
     * Calls for change status of Guest Invitation and handles responses
     *
     * @param uri identification of guest invitation
     * @param statusString status to which invitation should be changed
     * @return InvitationDto of updated event
     */
    @PostMapping (value = "/guest/invitation/{uri}/statusChange/{statusString}")
    public InvitationDto changeGuestInvitationStatus (@PathVariable String uri, @PathVariable String statusString){
        try {
            StatusEnum status = StatusEnum.valueOf(statusString);
            return InvitationMapper.toDto(invitationService.changeGuestInvitation(uri,status));
        } catch (EntityNotFoundException | IllegalArgumentException | NonValidUriException e) {
            e.printStackTrace();
            if (e.getClass().equals(EntityNotFoundException.class))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage(),e);
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage(),e);
            }
        }
    }
}
