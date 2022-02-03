/*
 * EventMapper
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package cz.profinit.sportTeamManager.mappers;

import cz.profinit.sportTeamManager.dto.event.EventDto;
import cz.profinit.sportTeamManager.model.event.Event;
import cz.profinit.sportTeamManager.model.user.RegisteredUser;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Mapper class that allows mapping between data transfer objects and entities
 */
@Component
@Profile({"test", "Main", "stub_services"})
public class EventMapper {

    /**
     * Map Event class to EventDto.
     *
     * @param event Event that needs to be mapped.
     * @return EventDto representing given Event
     */
    public static EventDto toDto(Event event) {
        return new EventDto(event.getEntityId(), event.getDate(), event.getCapacity(), event.getIsCanceled(),
                PlaceMapper.toDto(event.getPlace()), UserMapper.mapRegisteredUserToRegisteredUserDTO(event.getCreatedBy()));
    }

    /**
     * Map EventDto to Event
     *
     * @param eventDto EventDto that needs to be mapped.
     * @return Event representing given EventDto
     */
    public static Event toEvent(EventDto eventDto) {
        //TODO předělat ID
        Event event = new Event(eventDto.getDate(), eventDto.getCapacity(), eventDto.isCanceled(), PlaceMapper.toPlace(eventDto.getPlace()), UserMapper.mapRegisteredUserDTOToRegisteredUser(eventDto.getCreatedBy()), new ArrayList<>(), new ArrayList<>());
        event.setEntityId(eventDto.getId());
        return event;
    }
}
