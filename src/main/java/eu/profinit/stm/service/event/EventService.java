/*
 * EventService
 *
 * 0.1
 *
 * Author: M. Halamka
 */
package eu.profinit.stm.service.event;

import eu.profinit.stm.dto.event.EventDto;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.event.Event;
import eu.profinit.stm.model.event.Message;
import eu.profinit.stm.model.invitation.Invitation;

import java.util.List;

/**
 * Service interface for Event business logic.
 */
public interface EventService {

    Event createNewEvent(EventDto eventDto) throws EntityAlreadyExistsException;
    Event updateEvent (EventDto eventDto, Long eventId) throws EntityNotFoundException;
    Event findEventById(Long id) throws EntityNotFoundException;
    Message addNewMessage (String email, String messageStr, Long eventId) throws EntityNotFoundException;
    List<Message> getAllMessages (Long eventId) throws EntityNotFoundException;
    Event changeEventStatus (Long eventId) throws EntityNotFoundException;
    Event addNewInvitation(Long eventId, Invitation invitation) throws EntityNotFoundException;
    List<Invitation> getAllInvitations (Long eventId) throws EntityNotFoundException;
}
