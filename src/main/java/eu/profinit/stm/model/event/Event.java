/*
 * Event
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package eu.profinit.stm.model.event;

import eu.profinit.stm.model.entity.Entity;
import eu.profinit.stm.model.invitation.Invitation;
import eu.profinit.stm.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class representing Event entity. Contains date, place, maxPersonAttendance, isCanceled, createdBy, listOfMessages and listOfInvitations
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event extends Entity implements Serializable {

    private LocalDateTime date;
    private Integer capacity;
    private Boolean isCanceled;
    private Place place;
    private User createdBy;
    private List<Message> messageList;
    private List <Invitation> invitationList;

    /**
     * Adds new message to the Event
     * @param message Message that needs to be saved.
     */
    public void addNewMessage (Message message) {
        if (!messageList.contains(message)) {
            messageList.add(message);
        }
    }

    /**
     * Adds new invitation to the Event
     * @param invitation Invitation that needs to be saved.
     */
    public void addNewInvitation (Invitation invitation) {
        if (!invitationList.contains(invitation)) {
            invitationList.add(invitation);
        }
    }
}
