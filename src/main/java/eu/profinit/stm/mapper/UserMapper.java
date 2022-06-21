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
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Provides mapping from User data transfer object to User entity in both ways.
 */
@Component
@Profile({"test", "Main", "stub_services"})
@NoArgsConstructor
public class UserMapper {

    /**
     * Maps a single User to its data transfer object.
     *
     * @param user User to be mapped
     * @return mapped User to User  data transfer object
     */
    public static UserDto mapUserToUserDTO(User user) {
        return new UserDto(user.getName(),
                user.getSurname(),
                user.getEmail());
    }

    /**
     * Maps a single User data transfer object to a single User.
     *
     * @param userDTO User data transfer object to be mapped
     * @return mapped User data transfer object to User
     */
    public static User mapUserDTOToUser(UserDto userDTO) {
        return new User(userDTO.getName(),
                userDTO.getSurname(),
                null,
                userDTO.getEmail());
    }

    /**
     * Maps list of  User to its data transfer object.
     *
     * @param userList list of User to be mapped
     * @return mapped list of User to list of User data transfer object
     */
    public static List<UserDto> mapUserListToUserDTOList(List<User> userList) {
        return userList.stream().map(UserMapper::mapUserToUserDTO).collect(Collectors.toList());
    }

    /**
     * Maps a list of User data transfer object to list of User.
     *
     * @param UserList list of dUser data transfer object to be mapped
     * @return mapped list of User data transfer object to list of User
     */
    public static List<User> mapUserDTOListToUserList(List<UserDto> UserList) {
        return UserList.stream().map(UserMapper::mapUserDTOToUser).collect(Collectors.toList());
    }

    /**
     * Maps a UserDetailsDTO to user.
     *
     * @param userDetailsDTO user details data transfer object to be mapped
     * @return mapped User
     */
    public static User mapUserDetailsDTOToUser(UserDetailsDto userDetailsDTO) {
        return new User(userDetailsDTO.getUserName(),
                userDetailsDTO.getSurname(),
                userDetailsDTO.getPassword(),
                userDetailsDTO.getName());
    }

}
