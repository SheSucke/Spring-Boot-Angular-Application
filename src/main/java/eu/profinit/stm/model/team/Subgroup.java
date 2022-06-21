/*
 * Subgroup
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.model.team;

import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.entity.Entity;
import eu.profinit.stm.model.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Class containing subgroup information such as subgroup name and user list.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
public class Subgroup extends Entity implements Serializable {
    private String name;
    private List<User> userList;
    private Long teamId;

    /**
     * Sole class constructor.
     *
     * @param name name of the subgroup
     * @param teamId id of parent team
     */
    public Subgroup(String name, Long teamId) {
        this.name = name;
        this.teamId = teamId;
        this.userList = new ArrayList<>();
    }

    /**
     * Adds user to the user list.
     *
     * @param user user which should be added
     */
    public void addUser(User user) {
        userList.add(user);
    }

    /**
     * Removes user from a subgroup if the user is in subgroup, if not throws runtime exception.
     *
     * @param user user which should be removed
     */
    public void removeUser(User user) throws EntityNotFoundException {
        if (userList.contains(user)) {
            userList.remove(user);
        } else {
            throw new EntityNotFoundException("User");
        }
    }

    /**
     * find user in list by email
     * @param email to find user by
     * @return found user
     * @throws EntityNotFoundException if no user was found
     */
    public User getUser(String email) throws EntityNotFoundException {
        for (User regUs : userList) {
            if (regUs.getEmail().equals(email)) {
                return regUs;
            }
        }
        throw new EntityNotFoundException("User");
    }

    /**
     * Checks if user is in the list.
     *
     * @param user user which is looked for
     * @return true if user is in subgroup, false if not.
     */
    public boolean isUserInList(User user) {
        return userList.contains(user);
    }
}
