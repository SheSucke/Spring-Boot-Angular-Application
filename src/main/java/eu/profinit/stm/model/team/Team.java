/*
 * Team
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.model.team;

import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.entity.Entity;
import eu.profinit.stm.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Class containing all team information such as team name, team sport, list of subgroups and owner.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team extends Entity implements Serializable {
    private String name;
    private String sport;
    private List<Subgroup> subgroupList;
    private User owner;

    /**
     * Create a new subgroup and adds it to the list of subgroups.
     *
     * @param subgroupName name of the subgroup
     */
    public void addNewSubgroup(String subgroupName) {
        subgroupList.add(new Subgroup(subgroupName, this.getEntityId()));
    }

    /**
     * Obtains a subgroup defined by name from a list of the subgroups.
     *
     * @param subgroupName name of the subgroup
     * @return found subgroup
     */

    public Subgroup getTeamSubgroup(String subgroupName) throws EntityNotFoundException {
        for (Subgroup subgroup : subgroupList) {
            if (subgroup.getName().equals(subgroupName)) {
                return subgroup;
            }
        }
        throw new EntityNotFoundException("Subgroup");
    }

    /**
     * Removes a subgroup defined by name from
     *
     * @param subgroupName subgroup name what should be removed
     */
    public void deleteSubgroup(String subgroupName) throws EntityNotFoundException {
        Subgroup subgroup = getTeamSubgroup(subgroupName);
        subgroupList.remove(subgroup);
    }

    /**
     * Checks if a subgroup is in team by subgroup name.
     *
     * @param subgroupName string of subgroup name
     * @return true if subgroup of selected name is in team, false otherwise
     */
    public boolean isSubgroupInTeam(String subgroupName) {
        try {
            this.getTeamSubgroup(subgroupName);
        } catch (EntityNotFoundException e) {
            return false;
        }
        return true;
    }
}
