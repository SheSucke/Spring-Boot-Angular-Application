/*
 * StubEventService
 *
 * 0.1
 *
 * Author: M. Halamka
 */package cz.profinit.sportTeamManager.service.event;

import cz.profinit.sportTeamManager.dto.event.EventDto;
import cz.profinit.sportTeamManager.exception.EntityAlreadyExistsException;
import cz.profinit.sportTeamManager.exception.EntityNotFoundException;
import cz.profinit.sportTeamManager.mapper.EventMapper;
import cz.profinit.sportTeamManager.mapper.PlaceMapper;
import cz.profinit.sportTeamManager.model.event.Event;
import cz.profinit.sportTeamManager.model.event.Message;
import cz.profinit.sportTeamManager.model.invitation.Invitation;
import cz.profinit.sportTeamManager.model.user.RegisteredUser;
import cz.profinit.sportTeamManager.repository.event.EventRepository;
import cz.profinit.sportTeamManager.repository.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Stub service handling Event and Message entities.
 * Contains methods that creates and modifies Event and Message entities or
 * finds Event entity.
 */
@Service
public class EventServiceStub implements EventService {

    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    /**
     * Adds a new Event into database created from EventDto object.
     *
     * @param eventDto EventDto class, from which is new Event created
     * @return Event that was saved into database
     */
    @Override
    public Event createNewEvent(EventDto eventDto) throws EntityAlreadyExistsException {
        Event event = EventMapper.toEvent(eventDto);
        eventRepository.insertEvent(event);
        return event;
    }

    /**
     * Updates event to correspond with given DTO
     *
     * @param eventDto EvenDto class, from which is Event updated.
     * @param eventId Id of event that should be updated
     * @return Updated event
     * @throws EntityNotFoundException if event was not found
     */
    @Override
    public Event updateEvent(EventDto eventDto, Long eventId) throws EntityNotFoundException {
        Event event = findEventById(eventId);
        event.setDate(eventDto.getDate());
        event.setPlace(PlaceMapper.toPlace(eventDto.getPlace()));
        event.setCapacity(eventDto.getCapacity());
        event.setIsCanceled(eventDto.isCanceled());
        eventRepository.updateEvent(event);
        return event;
    }

    /**
     * Finds Entity in database by ID.
     *
     * @param id which is used to find Event entity in database
     * @return found Event
     * @throws EntityNotFoundException if entity is not found.
     */
    @Override
    public Event findEventById(Long id) throws EntityNotFoundException {
        return eventRepository.findEventById(id);
    }

    /**
     * Adds new message to the database and to the event
     *
     * @param email of user who is a sender of the message
     * @param messageStr sent message
     * @param eventId ID of event where message will be store
     * @return updated event
     * @throws EntityNotFoundException if entity is not found.
     */
    @Override
    public Message addNewMessage(String email, String messageStr, Long eventId) throws EntityNotFoundException {
        RegisteredUser user = userRepository.findUserByEmail(email);
        Event event =  findEventById(eventId);
        Message message = new Message(user, messageStr, LocalDateTime.now(), eventId);
        event.addNewMessage(message);
        eventRepository.updateEvent(event);
        return message;
    }

    /**
     * Returns all messages related to given event.
     *
     * @param eventId ID of event which from we want to get messages.
     * @return List of DTO of message.
     * @throws EntityNotFoundException if entity is not found.
     */
    @Override
    public List<Message> getAllMessages(Long eventId) throws EntityNotFoundException {
        return findEventById(eventId).getMessageList();
    }

    /**
     * Changes event status isCanceled. If event is going to be Canceled method changes isCanceled boolean to true;
     *
     * @param eventId ID if event that is going to be changed
     * @return changed Event
     * @throws EntityNotFoundException if entity is not found.
     */
    @Override
    public Event changeEventStatus(Long eventId) throws EntityNotFoundException {
        Event event = findEventById(eventId);
        event.setIsCanceled(!event.getIsCanceled());
        eventRepository.updateEvent(event);
        return event;
    }

    /**
     * Adds new Invitation to the Event
     *
     * @param eventId ID of event to which we want to add Invitation
     * @param invitation Invitation which we want to add to the event
     * @return updated event
     * @throws EntityNotFoundException if entity is not found.
     */
    @Override
    public Event addNewInvitation(Long eventId, Invitation invitation) throws EntityNotFoundException {
        Event event = findEventById(eventId);
        event.addNewInvitation(invitation);
        eventRepository.updateEvent(event);
        return event;
    }

    /**
     * Returns all invitation related to given Event
     * @param eventId ID of event for which we are getting Invitations
     * @return List of Invitations
     * @throws EntityNotFoundException if entity is not found.
     */
    @Override
    public List<Invitation> getAllInvitations(Long eventId) throws EntityNotFoundException {
        return findEventById(eventId).getInvitationList();
    }
}
