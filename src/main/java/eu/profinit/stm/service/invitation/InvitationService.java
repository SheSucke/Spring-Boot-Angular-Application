/*
 * InvitationService
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package eu.profinit.stm.service.invitation;

import eu.profinit.stm.dto.invitation.InvitationDto;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.exception.NonValidUriException;
import eu.profinit.stm.model.invitation.Invitation;
import eu.profinit.stm.model.invitation.StatusEnum;
import eu.profinit.stm.model.user.User;

import java.util.List;

public interface InvitationService {

    Invitation createNewInvitation(String email, Long eventId) throws EntityNotFoundException, EntityAlreadyExistsException;
    Invitation changeInvitationStatus(Long eventId, String email, StatusEnum status) throws EntityNotFoundException;
    Invitation findInvitationByEventIdAndEmail(Long eventId,String email) throws EntityNotFoundException;
    List<Invitation> createNewInvitationsFromList (List<User> userList, Long eventId)
            throws EntityNotFoundException, EntityAlreadyExistsException;
    List<InvitationDto> OrderListOfInvitationByDateForSpecificStatus (List<InvitationDto> invitationDtoList, StatusEnum status);
    void deleteInvitation (String email, Long eventId) throws EntityNotFoundException;
    Invitation getGuestInvitation (String uri) throws EntityNotFoundException, NonValidUriException;
    Invitation createGuestInvitation (Long eventId, String name) throws EntityNotFoundException, EntityAlreadyExistsException;
    Invitation changeGuestInvitation (String uri, StatusEnum status) throws NonValidUriException, EntityNotFoundException;
}
