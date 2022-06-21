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

import static org.junit.Assert.assertEquals;

public class SubgroupMapperTest {
    private Subgroup subgroup1;
    private List<Subgroup> subgroupList;
    private SubgroupDto subgroupDto1;
    private List<SubgroupDto> subgroupDtoList;
    private TeamDto teamDTO;
    private Team team;

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
        subgroup1 = new Subgroup("Players", 10L);
        subgroup1.setUserList(userList);
        Subgroup subgroup2 = new Subgroup("Coaches", 10L);
        subgroup2.setUserList(userList2);
        subgroupDto1 = new SubgroupDto("Players", 10L);
        subgroupDto1.setUserList(userDtoList);
        SubgroupDto subgroupDto2 = new SubgroupDto("Coaches", 10L);
        subgroupDto2.setUserList(userDtoList2);
        subgroupList = new ArrayList<>();
        subgroupList.add(subgroup1);
        subgroupList.add(subgroup2);
        subgroupDtoList = new ArrayList<>();
        subgroupDtoList.add(subgroupDto1);
        subgroupDtoList.add(subgroupDto2);
    }

    @Test
    public void mapSubgroupDTOToSubgroup() {
        Assert.assertEquals(subgroup1, SubgroupMapper.mapSubgroupDTOToSubgroup(subgroupDto1));
    }

    @Test
    public void mapSubgroupToSubgroupDTO() {
        Assert.assertEquals(subgroupDto1, SubgroupMapper.mapSubgroupToSubgroupDTO(subgroup1));
    }

    @Test
    public void mapSubgroupDTOListToSubgroupList() {
        assertEquals(subgroupList, SubgroupMapper.mapSubgroupDTOListToSubgroupList(subgroupDtoList));
    }

    @Test
    public void mapSubgroupListToSubgroupDTOList() {
        assertEquals(subgroupDtoList, SubgroupMapper.mapSubgroupListToSubgroupDTOList(subgroupList));
    }
}