package eu.profinit.stm.mapperMyBatis.invitation;

import eu.profinit.stm.model.invitation.Invitation;

public interface InvitationMapperMyBatis {

    /**
     * Insert Invitation to database and update it's entityId
     * @param invitation to insert
     */
    void insertInvitation(Invitation invitation);

    /**
     * Updates only changed LocalDateTime and status enum
     * @param invitation to update
     */
    void updateInvitation(Invitation invitation);

    /**
     *
     * @param id to find Invitation by
     */
    void deleteInvitationById(Long id);

    /**
     *
     * @param id to find Invitation by
     * @return found Invitation or null
     */
    Invitation findInvitationById(Long id);

    /**
     *
     * @param eventId parent event of a invitation
     * @param userId user recipient of invitation
     * @return found Invitation or null
     */
    Invitation findInvitationByEventIdAndUserId(Long eventId, Long userId);
}
