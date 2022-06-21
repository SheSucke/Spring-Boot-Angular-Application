/*
 * StubEventRepository
 *
 * 0.1
 *
 * Author: M. Halamka
 */


package eu.profinit.stm.repository.event;

import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.event.Event;
import eu.profinit.stm.model.event.Message;
import eu.profinit.stm.model.event.Place;
import eu.profinit.stm.model.invitation.Invitation;
import eu.profinit.stm.model.invitation.StatusEnum;
import eu.profinit.stm.model.user.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
@Profile("stub_repository")
public class EventRepositoryStub implements EventRepository {

    Event event;
    User loggedUser;

    public EventRepositoryStub() {
        Place place = new Place("Profinit","Tychonova 2", 1L);
        loggedUser = new User("Ivan", "Stastny", "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "is@gmail.com");
        event = new Event(LocalDateTime.now(), 6, false, place, loggedUser, new ArrayList<>(), new ArrayList<>());
        event.setEntityId(0L);
        event.getMessageList().add(new Message(loggedUser,"Testuji",LocalDateTime.now(), event.getEntityId()));
        event.getInvitationList().add(new Invitation(LocalDateTime.now(), LocalDateTime.now(), StatusEnum.PENDING, loggedUser, event.getEntityId()));
    }

    @Override
    public void insertEvent(Event event) {
        this.event = event;
    }

    @Override
    public Event findEventById(Long id) throws EntityNotFoundException {
        if (id.equals(event.getEntityId())){
            return event;
        } else {
            throw new EntityNotFoundException("Event");
        }
    }

    @Override
    public void updateEvent(Event event) {
        this.event = event;
    }
}
