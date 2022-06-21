/*
 * InvitationMapper
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package eu.profinit.stm.mapper;

import eu.profinit.stm.dto.invitation.InvitationDto;
import eu.profinit.stm.model.invitation.Invitation;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Mapper class that allows mapping between data transfer objects and entities
 */
@Component
@Profile({"test", "Main","stub_services"})
public class InvitationMapper {

    /**
     * Map Invitation class to InvitationDto.
     *
     * @param invitation Invitation that needs to be mapped.
     * @return InvitationDto representing given Event
     */
    public static InvitationDto toDto(Invitation invitation) {
        return new InvitationDto(invitation.getCreated(),invitation.getChanged(),invitation.getStatus(),invitation.getRecipient(),invitation.getEventId());
    }

    /**
     * Map InvitationDto to Invitation
     *
     * @param invitationDto InvitationDto that needs to be mapped.
     * @return Invitation representing given InvitationDto
     */
    public static Invitation toInvitation(InvitationDto invitationDto) {
        return new Invitation(invitationDto.getCreated(),invitationDto.getChanged(),invitationDto.getStatus(),invitationDto.getIsFor(),invitationDto.getEventId());
    }

    /**
     * Creates list of DTOs from Entity list
     *
     * @param invitationList given Entity list
     * @return list of DTOs
     */
    public static List<InvitationDto> toDtoList(List<Invitation> invitationList) {
        List<InvitationDto> invitationDtoList = new ArrayList<>();

        for (Invitation invitation : invitationList){
            invitationDtoList.add(InvitationMapper.toDto(invitation));
        }

        return  invitationDtoList;
    }

}
