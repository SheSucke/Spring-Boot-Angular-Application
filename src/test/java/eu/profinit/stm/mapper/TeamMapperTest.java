package eu.profinit.stm.mapper;

import eu.profinit.stm.dto.team.SubgroupDto;
import eu.profinit.stm.dto.team.TeamDto;
import eu.profinit.stm.model.team.Subgroup;
import eu.profinit.stm.model.team.Team;
import eu.profinit.stm.dto.user.UserDto;
import eu.profinit.stm.model.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TeamMapperTest {
    private Team team;
    private TeamDto teamDTO;

    @Before
    public void setUp() {
        User user1 = new User("Tomas", "Smutny", null, "ts@gmail.com");
        User user2 = new User("Ivan", "Stastny", null, "is@gmail.com");
        UserDto userDto1 = new UserDto("Tomas", "Smutny", "ts@gmail.com");
        UserDto userDto2 = new UserDto("Ivan", "Stastny", "is@gmail.com");
        List<User> userList = new ArrayList<>();
        List<User> userList2 = new ArrayList<>();
        List<UserDto> userDtoList = new ArrayList<>();
        List<UserDto> userDtoList2 = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList2.add(user1);
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);
        userDtoList2.add(userDto1);

        List<Subgroup> subgroupList = new ArrayList<>();
        List<SubgroupDto> subgroupDtoList = new ArrayList<>();
        team = new Team("A team", "golf", subgroupList, user1);
        team.setEntityId(10L);
        teamDTO = new TeamDto(10L, "A team", "golf", subgroupDtoList, userDto1);

        Subgroup subgroup1 = new Subgroup("Players", team.getEntityId());
        subgroup1.setUserList(userList);
        Subgroup subgroup2 = new Subgroup("Coaches", team.getEntityId());
        subgroup2.setUserList(userList2);
        SubgroupDto subgroupDto1 = new SubgroupDto("Players", teamDTO.getId());
        subgroupDto1.setUserList(userDtoList);
        SubgroupDto subgroupDto2 = new SubgroupDto("Coaches", teamDTO.getId());
        subgroupDto2.setUserList(userDtoList2);

        subgroupList.add(subgroup1);
        subgroupList.add(subgroup2);
        subgroupDtoList.add(subgroupDto1);
        subgroupDtoList.add(subgroupDto2);
        team.setSubgroupList(subgroupList);
        teamDTO.setListOfSubgroups(subgroupDtoList);
    }

    @Test
    public void mapTeamDtoToTeam() {
        Assert.assertEquals(team, TeamMapper.mapTeamDtoToTeam(teamDTO));
    }

    @Test
    public void mapTeamToTeamDto() {
        Assert.assertEquals(teamDTO, TeamMapper.mapTeamToTeamDto(team));
    }
}