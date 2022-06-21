/*
 * Team service
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.service.team;


import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.team.Team;
import eu.profinit.stm.model.user.User;

/**
 * Team service interface.
 */
public interface TeamService {
    /**
     * Create a new team with entered name and sport owned by currently logged registered user.
     * Team is created with two default subgroups: "All users" and "Coaches". Owner is added to both subgroups.
     *
     * @param team name of created team
     * @return newly created team with two default subgroups
     */
    Team createNewTeam(Team team);

    /**
     * Change team name to a new one.
     *
     * @param teamId  team what name should be changed
     * @param newName new name of a team
     */
    Team changeTeamName(Long teamId, String newName) throws EntityNotFoundException;

    /**
     * Change team sport to a new one.
     *
     * @param teamId   team what sport should be changed
     * @param newSport new sport of a team
     */
    Team changeTeamSport(Long teamId, String newSport) throws EntityNotFoundException;

    /**
     * Change team owner to a new one. Owner must be from All Users subgroup.
     *
     * @param teamId team what name should be changed
     * @param user   new owner of a team
     * @throws EntityNotFoundException if user is not found
     * @throws RuntimeException        if user is not in All Users subgroup
     */
    Team changeTeamOwner(Long teamId, User user) throws EntityNotFoundException, EntityAlreadyExistsException;

    /**
     * Gets team from repository
     *
     * @param teamId Id of team which we want to get
     * @return retrieved team
     */
    Team getTeamById(Long teamId) throws EntityNotFoundException;

    /**
     * Adds a new user to the subgroup in certain team.
     * Checks if the added user is in the default subgroup "All users". If not, user is also added to this subgroup.
     * If the user is in the subgroup, throws Exception.
     *
     * @param teamId       team where is a subgroup
     * @param subgroupName name of subgroup where we want to add
     * @param user         user which we want to add to the subgroup
     * @return team with updated subgroup now including added user
     */
    Team addUserToSubgroup(Long teamId, String subgroupName, User user) throws EntityNotFoundException, EntityAlreadyExistsException;

    /**
     * Adds a user to the team by adding it to the default subgroup "All users".
     * Checks if the user is already in the subgroup. If the user is in the subgroup, throws Exception.
     *
     * @param teamId team where user should be added
     * @param user   user which should be added
     * @return updated team with added user
     */
    Team addUserToTeam(Long teamId, User user) throws EntityNotFoundException, EntityAlreadyExistsException;

    /**
     * Adds a new subgroup of selected id to the determined team. Team is selected by its name.
     *
     * @param teamId       team where the subgroup should be added
     * @param subgroupName name of the new subgroup
     * @return team with including added subgroup
     */
    Team addSubgroup(Long teamId, String subgroupName) throws EntityNotFoundException, EntityAlreadyExistsException;

    /**
     * Removes a subgroup of determined name from the team.
     *
     * @param teamId       team id from which subgroup should be removed
     * @param subgroupName name of the subgroup what should be removed
     * @return team excluding removed subgroup
     */
    Team deleteSubgroup(Long teamId, String subgroupName) throws EntityNotFoundException;

    /**
     * Removes a user from subgroup determined by name.
     *
     * @param teamId       name of id containing a subgroup from which user should be removed
     * @param subgroupName name of the subgroup from which user should be removed
     * @param user         user which should be removed
     * @return team with updated subgroup excluding a removed user
     */
    Team deleteUserFromSubgroup(Long teamId, String subgroupName, User user) throws EntityNotFoundException;

    /**
     * Removes user form all subgroups of determined team.
     *
     * @param teamId team from which user should be removed
     * @param user   user which should be removed
     * @return team excluding a removed user
     */
    Team deleteUserFromTeam(Long teamId, User user) throws EntityNotFoundException;

    /**
     * Removes team from database
     *
     * @param teamId team which should be removed
     */
    void deleteTeam(Long teamId) throws EntityNotFoundException;


    /**
     * Changes name of selected subgroup in a team
     *
     * @param teamId       team where subgroup is
     * @param subgroupName name of subgroup
     * @param newName      new name of subgroup
     * @return updated team
     */
    Team changeSubgroupName(Long teamId, String subgroupName, String newName) throws EntityNotFoundException, EntityAlreadyExistsException;

}
