/*
 * TeamServiceImpl
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.service.team;

import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.exception.UserIsInSubgroupException;
import eu.profinit.stm.model.team.Subgroup;
import eu.profinit.stm.model.team.Team;
import eu.profinit.stm.repository.subgroup.SubgroupRepository;
import eu.profinit.stm.repository.team.TeamRepository;
import eu.profinit.stm.repository.user.UserRepository;
import eu.profinit.stm.model.user.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service manipulating with team and subgroup.
 * This service contains methods that can create a new team,
 * add/deleteTeam subgroup to the team
 * and add/deleteTeam user to team or subgroup.
 */
@Service
@AllArgsConstructor
@Profile("Main")
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubgroupRepository subgroupRepository;

    private static final Logger LOG = LoggerFactory.getLogger(TeamServiceImpl.class);

    private static final String ALL_USER_SUBGROUP = "All Users";
    private static final String COACHES_SUBGROUP = "Coaches";


    /**
     * Create a new team with entered name and sport owned by currently logged registered user.
     * Team is created with two default subgroups: "All users" and "Coaches". Owner is added to both subgroups.
     *
     * @param team name of created team
     * @return newly created team with two default subgroups
     */
    public Team createNewTeam(Team team) {
        teamRepository.insertTeam(team);
        team.addNewSubgroup(ALL_USER_SUBGROUP);
        team.addNewSubgroup(COACHES_SUBGROUP);
        for (int i = 0;i < 2;i++)
        {
            Subgroup subgroup = team.getSubgroupList().get(i);
            subgroup.setTeamId(team.getEntityId());
            subgroup.addUser(team.getOwner());
            try {
                subgroupRepository.insertSubgroup(subgroup);
            } catch (EntityAlreadyExistsException e) {
                LOG.debug(e.getMessage());
            }

        }
        return team;

    }

    /**
     * Change team name to a new one.
     *
     * @param teamId  team id which name should be changed
     * @param newName new name of a team
     */
    public Team changeTeamName(Long teamId, String newName) throws EntityNotFoundException {
        Team team = getTeamById(teamId);
        team.setName(newName);
        teamRepository.updateTeam(team);
        return team;
    }

    /**
     * Change team sport to a new one.
     *
     * @param teamId   of team for which sport should be changed
     * @param newSport new sport of a team
     */
    public Team changeTeamSport(Long teamId, String newSport) throws EntityNotFoundException {
        Team team = getTeamById(teamId);
        team.setSport(newSport);
        teamRepository.updateTeam(team);
        return team;
    }

    /**
     * Change team owner to a new one. Owner must be from All Users subgroup.
     *
     * @param teamId of team what name should be changed
     * @param user new owner of a team
     * @throws EntityNotFoundException if user is not found
     * @throws RuntimeException if user is not in All Users subgroup
     */
    public Team changeTeamOwner(Long teamId, User user) throws EntityNotFoundException, EntityAlreadyExistsException {
        Team team = getTeamById(teamId);
        if (!team.getTeamSubgroup(ALL_USER_SUBGROUP).isUserInList(user)) {
            throw new EntityNotFoundException("User");
        } else if (!team.getTeamSubgroup(COACHES_SUBGROUP).isUserInList(user)) {
            team = addUserToSubgroup(team.getEntityId(), COACHES_SUBGROUP, user);
        }
        team.setOwner(user);
        teamRepository.updateTeam(team);
        return team;
    }

    /**
     * Calls a repository to found a team in database by team name.
     *
     * @param teamName name of search team
     * @return founded team of searched name
     */
    public List<Team> getTeamByName(String teamName) throws EntityNotFoundException {
        return teamRepository.findTeamsByName(teamName);
    }

    /**
     * Adds a new user to the subgroup in certain team.
     * Checks if the added user is in the default subgroup "All users". If not, user is also added to this subgroup.
     * If the user is in the subgroup, throws Exception.
     *
     * @param teamId of team where is a subgroup
     * @param subgroupName name of subgroup where we want to add
     * @param user user which we want to add to the subgroup
     * @return team with updated subgroup now including added user
     * @throws EntityNotFoundException   when user is not found
     * @throws UserIsInSubgroupException when user is already in subgroup
     */
    public Team addUserToSubgroup(Long teamId, String subgroupName, User user) throws EntityAlreadyExistsException, EntityNotFoundException {
        Team team = getTeamById(teamId);
        user = userRepository.findUserByEmail(user.getEmail());

        if (!team.getTeamSubgroup(ALL_USER_SUBGROUP).isUserInList(user)) {
            team = addUserToTeam(team.getEntityId(), user);
        }

        if (!subgroupName.equals(ALL_USER_SUBGROUP) && team.getTeamSubgroup(subgroupName).isUserInList(user)) {
            throw new EntityAlreadyExistsException("User");
        } else {
            Subgroup subgroup = team.getTeamSubgroup(subgroupName);
            subgroup.addUser(user);
            subgroupRepository.updateSubgroup(subgroup);
        }

        return team;
    }

    /**
     * Adds a user to the team by adding it to the default subgroup "All users".
     * Checks if the user is already in the subgroup. If the user is in the subgroup, throws Exception.
     *
     * @param teamId of team where user should be added
     * @param user user which should be added
     * @return updated team with added user
     * @throws EntityNotFoundException when user is not found
     */
    public Team addUserToTeam(Long teamId, User user) throws EntityAlreadyExistsException, EntityNotFoundException {
        Team team = getTeamById(teamId);
        user = userRepository.findUserByEmail(user.getEmail());
        if (team.getTeamSubgroup(ALL_USER_SUBGROUP).isUserInList(user)) {
            throw new EntityAlreadyExistsException("User");
        } else {
            Subgroup subgroup = team.getTeamSubgroup(ALL_USER_SUBGROUP);
            subgroup.addUser(user);
            subgroupRepository.updateSubgroup(subgroup);
        }
        return team;
    }

    /**
     * Adds a new subgroup of selected name to the determined team. Team is selected by its name.
     *
     * @param teamId of team where the subgroup should be added
     * @param subgroupName name of the new subgroup
     * @return team with including added subgroup
     */
    public Team addSubgroup(Long teamId, String subgroupName) throws EntityNotFoundException, EntityAlreadyExistsException {
        Team team = getTeamById(teamId);
        if (team.isSubgroupInTeam(subgroupName)) {
            throw new EntityAlreadyExistsException("Subgroup");
        } else {
            Subgroup subgroup = new Subgroup(subgroupName,teamId);
            subgroupRepository.insertSubgroup(subgroup);
            team.addNewSubgroup(subgroupName);
        }

        return team;
    }

    /**
     * Removes a subgroup of determined name from the team.
     *
     * @param teamId of team from which subgroup should be removed
     * @param subgroupName name of the subgroup what should be removed
     * @return team excluding removed subgroup
     * @throws EntityNotFoundException when subgroup is not found
     */
    public Team deleteSubgroup(Long teamId, String subgroupName) throws EntityNotFoundException {
        Team team = getTeamById(teamId);

        if (team.isSubgroupInTeam(subgroupName)) {
            Subgroup subgroup = team.getTeamSubgroup(subgroupName);
            subgroupRepository.deleteSubgroup(subgroup);
            team.deleteSubgroup(subgroupName);
        } else {
            throw new EntityNotFoundException("Subgroup");
        }

        return team;
    }

    /**
     * Removes a user from subgroup determined by name.
     *
     * @param teamId of team containing a subgroup from which user should be removed
     * @param subgroupName name of the subgroup from which user should be removed
     * @param user which should be removed
     * @return team with updated subgroup excluding a removed user
     * @throws EntityNotFoundException when user is not found
     */
    public Team deleteUserFromSubgroup(Long teamId, String subgroupName, User user) throws EntityNotFoundException {
        Team team = getTeamById(teamId);
        user = userRepository.findUserByEmail(user.getEmail());
        Subgroup subgroup = team.getTeamSubgroup(subgroupName);
        subgroup.removeUser(user);
        try {
            subgroupRepository.updateSubgroup(subgroup);
        } catch (EntityAlreadyExistsException e) {
            LOG.debug(e.getMessage());
        }
        return team;
    }

    /**
     * Removes user form all subgroups of determined team.
     *
     * @param teamId of team from which user should be removed
     * @param user which should be removed
     * @return team excluding a removed user
     * @throws EntityNotFoundException when user is not found
     */
    public Team deleteUserFromTeam(Long teamId, User user) throws EntityNotFoundException {
        Team team = getTeamById(teamId);
        user = userRepository.findUserById(user.getEntityId());

        if (!team.getTeamSubgroup(ALL_USER_SUBGROUP).isUserInList(user)) throw new EntityNotFoundException("User");

        for (Subgroup subgroup : team.getSubgroupList()) {
            if (subgroup.isUserInList(user)) {
                subgroup.removeUser(user);
                try {
                    subgroupRepository.updateSubgroup(subgroup);
                } catch (EntityAlreadyExistsException e) {
                    LOG.debug(e.getMessage());
                }
            }
        }
        return team;
    }

    /**
     * Deletes team from database
     *
     * @param teamId of team which we want to delete
     */
    public void deleteTeam(Long teamId) throws EntityNotFoundException {
        teamRepository.deleteTeam(getTeamById(teamId));
    }

    /**
     * Changes name of the subgroup to a new one. Checks if subgroup exists and if a new selected name
     * do not coincide with already existing subgroup.
     *
     * @param teamId of team where subgroup is
     * @param subgroupName name of subgroup
     * @param newName new name of subgroup
     * @return team with updated subgroup
     * @throws EntityNotFoundException when subgroup is not found
     * @throws EntityAlreadyExistsException when subgroup is already in team
     */
    public Team changeSubgroupName(Long teamId, String subgroupName, String newName) throws EntityNotFoundException, EntityAlreadyExistsException {
        Team team = getTeamById(teamId);

        if (team.isSubgroupInTeam(newName)) {
            throw new EntityAlreadyExistsException("Subgroup");
        } else if (!team.isSubgroupInTeam(subgroupName)) {
            throw new EntityNotFoundException("Subgroup");
        }

        for (Subgroup subgroup : team.getSubgroupList()) {
            if (subgroup.getName().equals(subgroupName)) {
                subgroup.setName(newName);
                try {
                    subgroupRepository.updateSubgroup(subgroup);
                } catch (EntityAlreadyExistsException e) {
                    LOG.debug(e.getMessage());
                }
            }
        }
        return team;
    }

    /**
     * Gets team from repository
     *
     * @param teamId of team that we want to get
     * @return retrieved team
     */
    public Team getTeamById(Long teamId) throws EntityNotFoundException {
        return teamRepository.findTeamById(teamId);
    }
}

