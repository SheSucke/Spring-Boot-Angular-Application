/*
 * TeamMapper
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.mapper;

import eu.profinit.stm.dto.team.TeamDto;
import eu.profinit.stm.model.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Maps a subgroup data transfer object to subgroup entity and back.
 */
@Component
@Profile({"test", "Main","stub_services"})
public class TeamMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SubgroupMapper subgroupMapper;


    /**
     * Maps a single teamDTO to team.
     *
     * @param teamDto team data transfer object to be mapped
     * @return mapped subgroup data transfer object to subgroup
     */
    public static Team mapTeamDtoToTeam(TeamDto teamDto) {
        Team team = new Team(teamDto.getName(),
                teamDto.getSport(),
                SubgroupMapper.mapSubgroupDTOListToSubgroupList(teamDto.getListOfSubgroups()),
                UserMapper.mapUserDTOToUser(teamDto.getOwner()));
        team.setEntityId(teamDto.getId());
        return team;
    }

    /**
     * Maps a single team to teamDTO.
     *
     * @param team team to be mapped
     * @return mapped subgroup data transfer object to subgroup
     */
    public static TeamDto mapTeamToTeamDto(Team team) {
        return new TeamDto(team.getEntityId(), team.getName(),
                team.getSport(),
                SubgroupMapper.mapSubgroupListToSubgroupDTOList(team.getSubgroupList()),
                UserMapper.mapUserToUserDTO(team.getOwner()));
    }
}
