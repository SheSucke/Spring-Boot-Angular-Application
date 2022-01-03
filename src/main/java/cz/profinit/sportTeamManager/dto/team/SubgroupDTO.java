/*
 * SubgroupDTO
 *
 * 0.1
 *
 * Author: J. Janský
 */

package cz.profinit.sportTeamManager.dto.team;

import cz.profinit.sportTeamManager.dto.user.RegisteredUserDTO;
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
public class SubgroupDTO {
    private String name;
    private List<RegisteredUserDTO> userList;

    public SubgroupDTO(String name) {
        this.name = name;
        this.userList = new ArrayList<>();
    }

    /**
     * Adds user to the user list.
     *
     * @param user user which should be added
     */
    public void addUser(RegisteredUserDTO user) {
        userList.add(user);
    }
}
