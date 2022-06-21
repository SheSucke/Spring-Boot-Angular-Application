/*
 * SubgroupMapper
 *
 * 0.1
 *
 * Author: J. Janský
 */

package eu.profinit.stm.mapper;


import eu.profinit.stm.dto.team.SubgroupDto;
import eu.profinit.stm.model.team.Subgroup;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Maps a subgroup data transfer object to subgroup entity and back.
 */
@Component
@Profile({"test", "Main","stub_services"})
public class SubgroupMapper {

    /**
     * Maps a single subgroupDTO to subgroup.
     *
     * @param subgroupDTO subgroup data transfer object to be mapped
     * @return mapped subgroup data transfer object to subgroup
     */
    public static Subgroup mapSubgroupDTOToSubgroup(SubgroupDto subgroupDTO) {
        Subgroup subgroup = new Subgroup(subgroupDTO.getName(), subgroupDTO.getTeamId());
        subgroup.setUserList(UserMapper.mapUserDTOListToUserList(subgroupDTO.getUserList()));
        return subgroup;
    }

    /**
     * Maps a single subgroup to subgroupDTO.
     *
     * @param subgroup subgroup to be mapped
     * @return mapped subgroup to  subgroup data transfer object
     */
    public static SubgroupDto mapSubgroupToSubgroupDTO(Subgroup subgroup) {
        SubgroupDto subgroupDTO = new SubgroupDto(subgroup.getName(), subgroup.getTeamId());
        subgroupDTO.setUserList(UserMapper.mapUserListToUserDTOList(subgroup.getUserList()));
        return subgroupDTO;
    }

    /**
     * Maps a list of subgroupDTOs to list of subgroups.
     *
     * @param subgroupDtoList list of subgroup data transfer objects to be mapped
     * @return mapped list of subgroup data transfer objects to list of subgroups
     */
    public static List<Subgroup> mapSubgroupDTOListToSubgroupList(List<SubgroupDto> subgroupDtoList) {
        return subgroupDtoList.stream().map(SubgroupMapper::mapSubgroupDTOToSubgroup).collect(Collectors.toList());
    }

    /**
     * Maps a list of subgroups to list of subgroupDTOs.
     *
     * @param subgroupList list of subgroups to be mapped
     * @return mapped list of subgroups to list of subgroup data transfer objects
     */
    public static List<SubgroupDto> mapSubgroupListToSubgroupDTOList(List<Subgroup> subgroupList) {
        return subgroupList.stream().map(SubgroupMapper::mapSubgroupToSubgroupDTO).collect(Collectors.toList());
    }

}
