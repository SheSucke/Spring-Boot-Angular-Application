/*
 * Subgroup
 *
 * 0.1
 *
 * Author: J. Janský
 */
package cz.profinit.sportTeamManager.model.team;

import cz.profinit.sportTeamManager.model.entity.Entity;
import cz.profinit.sportTeamManager.model.user.RegisteredUser;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * Class containing subgroup information such as subgroup name and user list.
 */
@Data
public class Subgroup extends Entity {
    private String name;
    private List<RegisteredUser> userList;
    private Team team;


    /**
     * Sole class constructor.
     *
     * @param name name of the subgroup
     */
    public Subgroup(String name, Team team) {
        this.name = name;
        this.team = team;
        this.userList = new ArrayList<>();
    }


    /**
     * Adds user to the user list.
     *
     * @param user user which should be added
     */
    public void addUser(RegisteredUser user) {
        userList.add(user);
    }

    /**
     * Removes user from a subgroup if the user is in subgroup, if not throws runtime exception.
     *
     * @param user user which should be removed
     */
    public void removeUser(RegisteredUser user) {
        if (userList.contains(user)) {
            userList.remove(user);
        } else {
            throw new RuntimeException("no user in list");
        }
    }


    /**
     * Checks if user is in the list.
     *
     * @param user user which is looked for
     * @return true if user is in subgroup, false if not.
     */
    public boolean isUserInList(RegisteredUser user) {
        return userList.contains(user);
    }

    @Override
    public String toString() {
        return "Subgroup{" +
                "name='" + name + '\'' +
                ", userList=" + userList +
                ", team=" + team.getEntityId() +
                '}';
    }
}
