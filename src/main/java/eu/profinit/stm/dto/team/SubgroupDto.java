/*
 * SubgroupDTO
 *
 * 0.1
 *
 * Author: J. Janský
 */

package eu.profinit.stm.dto.team;

import eu.profinit.stm.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


/**
 * Data transfer object for Registered user entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SubgroupDto {
    private String name;
    private List<UserDto> userList;
    private Long teamId;

    public SubgroupDto(String name, Long teamId) {
        this.name = name;
        this.teamId = teamId;
        this.userList = new ArrayList<>();
    }

    /**
     * Adds user to the user list.
     *
     * @param user user which should be added
     */
    public void addUser(UserDto user) {
        userList.add(user);
    }
}
