/*
 * UserMapper
 *
 * 0.1
 *
 * Author: J. Janský
 */

package eu.profinit.stm.mapper;

import eu.profinit.stm.dto.user.UserDetailsDto;
import eu.profinit.stm.dto.user.UserDto;
import eu.profinit.stm.model.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for Subgroup class
 */
@RunWith(SpringRunner.class)
public class UserMapperTest {
    private User user1;
    private UserDto userDto1;
    private List<User> userList;
    private List<UserDto> userDtoList;
    private UserDetailsDto userDetailsDTO;

    @Before
    public void setUp() {
        user1 = new User("Tomas", "Smutny", "pass1", "ts@gmail.com");
        User user2 = new User("Ivan", "Stastny", "pass2", "is@gmail.com");
        userDto1 = new UserDto("Tomas", "Smutny", "ts@gmail.com");
        UserDto userDto2 = new UserDto("Ivan", "Stastny", "is@gmail.com");
        userList = new ArrayList<>();
        userDtoList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userDtoList.add(userDto1);
        userDtoList.add(userDto2);
        userDetailsDTO = new UserDetailsDto("ts@gmail.com", "pass1", "Tomas", "Smutny");
    }

    @Test
    public void mapUserToUserDTO() {
        Assert.assertEquals(userDto1, UserMapper.mapUserToUserDTO(user1));
    }

    @Test
    public void mapUserDTOToUser() {
        user1.setPassword(null);
        Assert.assertEquals(user1, UserMapper.mapUserDTOToUser(userDto1));
    }

    @Test
    public void mapUserDTOListToUserList() {
        userList.get(0).setPassword(null);
        userList.get(1).setPassword(null);
        assertEquals(userList, UserMapper.mapUserDTOListToUserList(userDtoList));

    }

    @Test
    public void mapUserListToUserDTOList() {
        assertEquals(userDtoList, UserMapper.mapUserListToUserDTOList(userList));
    }

    @Test
    public void mapUserDetailsDTOToUser() {
        Assert.assertEquals(user1, UserMapper.mapUserDetailsDTOToUser(userDetailsDTO));
    }
}