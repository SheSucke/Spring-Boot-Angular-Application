/*
 * InvitationRepository
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package eu.profinit.stm.repository.invitation;

import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.invitation.Invitation;

public interface InvitationRepository {

    /**
     *
     * @param invitation to insert
     * @throws EntityNotFoundException if recipient or event could not be found
     * @throws EntityAlreadyExistsException if an Invitation is already in database
     */
    void insertInvitation(Invitation invitation) throws EntityNotFoundException, EntityAlreadyExistsException;

    /**
     * updates only status and changed
     * @param invitation to update
     * @throws EntityNotFoundException if Invitation could not be found
     */
    void updateInvitation(Invitation invitation) throws EntityNotFoundException;

    /**
     *
     * @param id to find Invitation by
     * @return found Invitation or throw
     * @throws EntityNotFoundException if Invitation could not be found
     */
    Invitation findInvitationById(Long id) throws EntityNotFoundException;

    /**
     *
     * @param eventId of parent Event
     * @param userEmail email of a recipient user
     * @return found Invitation
     * @throws EntityNotFoundException if Invitation could not be found
     */
    Invitation findInvitationByEventIdAndUserEmail(Long eventId, String userEmail) throws EntityNotFoundException;

    /**
     *
     * @param userEmail of an Invitation recipient
     * @param eventId of parent Event
     * @throws EntityNotFoundException if Invitation could not be found
     */
    void deleteInvitation(String userEmail, Long eventId) throws EntityNotFoundException;

    /**
     *
     * @param userEmail of Invitation recipient
     * @param eventId of parent Event
     * @return whether is has user an Invitation to Event
     * @throws EntityNotFoundException if Invitation could not be found
     */
    boolean isUserInvitedToEvent(String userEmail, Long eventId) throws EntityNotFoundException;

}
