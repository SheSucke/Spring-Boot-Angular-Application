/*
 * TeamController
 *
 * 0.1
 *
 * Author: J. Janský
 */

package cz.profinit.sportTeamManager.controllers.team;

import cz.profinit.sportTeamManager.dto.team.TeamDTO;
import cz.profinit.sportTeamManager.exceptions.HttpExceptionHandler;
import cz.profinit.sportTeamManager.mappers.TeamMapper;
import cz.profinit.sportTeamManager.model.team.Team;
import cz.profinit.sportTeamManager.model.user.RegisteredUser;
import cz.profinit.sportTeamManager.oauth.PrincipalExtractorImpl;
import cz.profinit.sportTeamManager.service.team.TeamService;
import cz.profinit.sportTeamManager.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

/**
 * Maps incoming team requests.
 */
@RestController
@Profile({"test", "Main"})
public class TeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private PrincipalExtractorImpl principalExtractor;

    /**
     * Returns an updated team selected by id. If team does not exist throws exception.
     *
     * @param teamId team identification to retrieve team from database
     * @return updated team in form of data transfer object
     */
    @GetMapping("team/{teamId}")
    public TeamDTO refreshTeam(@PathVariable Long teamId) {
        Team team = new Team();
        try {
            team = teamService.getTeamById(teamId);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
        return TeamMapper.mapTeamToTeamDto(team);
    }


    /**
     * Creates a new team with selected name and sport
     *
     * @param name  name of a new team
     * @param sport sport of a new team
     * @return updated team in form of data transfer object
     */
    @PostMapping("/team/createTeam/{name}/{sport}")
    public TeamDTO createNewTeam(@PathVariable String name, @PathVariable String sport) {
        String email = principalExtractor.getPrincipalEmail();
        RegisteredUser user = null;
        try {
            user = userService.findUserByEmail(email);
        } catch (Exception e) {
            //empty never happens
        }
        Team newTeam = new Team(name, sport, new ArrayList<>(), user);
        newTeam = teamService.createNewTeam(newTeam);
        return TeamMapper.mapTeamToTeamDto(newTeam);
    }


    /**
     * Adds a new subgroup to the team.
     * Checks if a new subgroup name do not collide with already existing subgroup in team.
     * If not, throws BAD_REQUEST Http status.
     *
     * @param teamId       team identification to retrieve team from database
     * @param subgroupName name of a new subgroup
     * @return updated team in form of data transfer object
     */
    @PostMapping("team/{teamId}/subgroup/{subgroupName}")
    public TeamDTO addSubgroup(@PathVariable Long teamId, @PathVariable String subgroupName) {

        Team team = new Team();

        try {
            team = teamService.addSubgroup(teamId, subgroupName);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
        return TeamMapper.mapTeamToTeamDto(team);
    }

    /**
     * Renames a subgroup to selected name.
     * Checks if subgroup exists and if a new name do not collide with already existing subgroup.
     * If not, throws BAD_REQUEST Http status.
     *
     * @param teamId       team identification to retrieve team from database
     * @param subgroupName name of subgroup for renaming
     * @param newName      new name of renamed subgroup
     * @return updated team in form of data transfer object
     */
    @PutMapping("team/{teamId}/subgroup/{subgroupName}/{newName}")
    public TeamDTO renameSubgroup(@PathVariable Long teamId,
                                  @PathVariable String subgroupName,
                                  @PathVariable String newName) {

        Team team = new Team();

        try {
            team = teamService.changeSubgroupName(teamId, subgroupName, newName);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

        return TeamMapper.mapTeamToTeamDto(team);
    }

    /**
     * Deletes a subgroup from team. Checks if subgroup is in team. If not, throws BAD_REQUEST Http status.
     *
     * @param teamId       team identification to retrieve team from database
     * @param subgroupName name of subgroup for deletion
     * @return updated team in form of data transfer object
     */
    @DeleteMapping("team/{teamId}/subgroup/{subgroupName}")
    public TeamDTO deleteSubgroup(@PathVariable Long teamId, @PathVariable String subgroupName) {

        Team team = new Team();

        try {
            team = teamService.deleteSubgroup(teamId, subgroupName);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

        return TeamMapper.mapTeamToTeamDto(team);
    }

    /**
     * Adds a new user defined by email to the team. Checks if user of selected email exists.
     * If not, throws NOT_FOUND Http status. Also checks if user is not already in the team.
     * If is, throws BAD_REQUEST Http status.
     *
     * @param teamId    team identification to retrieve team from database
     * @param userEmail email of user who should be added
     * @return updated team in form of data transfer object
     */
    @PutMapping("team/{teamId}/user/{userEmail}")
    public TeamDTO addUserToTeam(@PathVariable Long teamId, @PathVariable String userEmail) {

        RegisteredUser user = new RegisteredUser();
        Team team = new Team();

        try {
            user = userService.findUserByEmail(userEmail);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

        try {
            team = teamService.addUserToTeam(teamId, user);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

        return TeamMapper.mapTeamToTeamDto(team);
    }

    /**
     * Removes a user defined by email from the team. Checks if user of selected email exists.
     * If not, throws NOT_FOUND Http status.
     *
     * @param teamId    team identification to retrieve team from database
     * @param userEmail email of the user who should be removed
     * @return updated team in form of data transfer object
     */
    @DeleteMapping("team/{teamId}/user/{userEmail}")
    public TeamDTO deleteUserFromTeam(@PathVariable Long teamId, @PathVariable String userEmail) {
        RegisteredUser user = new RegisteredUser();
        Team team = new Team();

        try {
            user = userService.findUserByEmail(userEmail);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
        try {
            team = teamService.deleteUserFromTeam(teamId, user);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

        return TeamMapper.mapTeamToTeamDto(team);
    }

    /**
     * Removes a user defined by email from a subgroup of team. Checks if user of selected email and subgroup exist.
     * If not, throws NOT_FOUND Http status.
     *
     * @param teamId       team identification to retrieve team from database
     * @param subgroupName name of subgroup from which user should be removed
     * @param userEmail    email of the user who should be removed
     * @return updated team in form of data transfer object
     */
    @DeleteMapping("team/{teamId}/{subgroupName}/user/{userEmail}")
    public TeamDTO deleteUserFromSubgroup(@PathVariable Long teamId,
                                          @PathVariable String subgroupName,
                                          @PathVariable String userEmail) {

        RegisteredUser user = new RegisteredUser();
        Team team = new Team();

        try {
            user = userService.findUserByEmail(userEmail);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
        try {
            team = teamService.deleteUserFromSubgroup(teamId, subgroupName, user);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

        return TeamMapper.mapTeamToTeamDto(team);
    }

    /**
     * Adds a new user defined by email to the subgroup of the team. Checks if user of selected email exists.
     * If not, throws NOT_FOUND Http status. Also checks if user is not already in the subgroup.
     * If is, throws BAD_REQUEST Http status.
     *
     * @param teamId       team identification to retrieve team from database
     * @param subgroupName name of subgroup to which user should be added
     * @param userEmail    email of the user who should be added
     * @return updated team in form of data transfer object
     */
    @PutMapping("team/{teamId}/{subgroupName}/user/{userEmail}")
    public TeamDTO addUserToSubgroup(@PathVariable Long teamId,
                                     @PathVariable String subgroupName,
                                     @PathVariable String userEmail) {

        RegisteredUser user = new RegisteredUser();
        Team team = new Team();

        try {
            user = userService.findUserByEmail(userEmail);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

        try {
            team = teamService.addUserToSubgroup(teamId, subgroupName, user);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

        return TeamMapper.mapTeamToTeamDto(team);
    }


    /**
     * Deletes a whole team.
     *
     * @param teamId team identification to retrieve team from database
     */
    @DeleteMapping("team/{teamId}")
    public void deleteTeam(@PathVariable Long teamId) {
        try {
            teamService.deleteTeam(teamId);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
    }


    /**
     * Renames team.
     *
     * @param teamId  team identification to retrieve team from database
     * @param newName new name of team
     * @return updated team in form of data transfer object
     */
    @PutMapping("team/{teamId}/teamName/{newName}")
    public TeamDTO changeTeamName(@PathVariable Long teamId, @PathVariable String newName) {
        Team team = new Team();
        try {
            team = teamService.changeTeamName(teamId, newName);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
        return TeamMapper.mapTeamToTeamDto(team);
    }

    /**
     * Changes a team sport.
     *
     * @param teamId   team identification to retrieve team from database
     * @param newSport new sport of a team
     * @return updated team in form of data transfer object
     */
    @PutMapping("team/{teamId}/teamSport/{newSport}")
    public TeamDTO changeTeamSport(@PathVariable Long teamId, @PathVariable String newSport) {
        Team team = new Team();
        try {
            team = teamService.changeTeamSport(teamId, newSport);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
        return TeamMapper.mapTeamToTeamDto(team);
    }

    /**
     * Changes a team owner defined by email. Checks if user of selected email exists.
     * If not, throws NOT_FOUND Http status. Also checks if a user is in subgroup All Users and Coaches
     * (important for consistency and user authorization purposes).
     * If user is not in subgroup All Users, throws BAD_REQUEST Http status.
     * If user is not in Coaches subgroup, he is automatically added.
     *
     * @param teamId        team identification to retrieve team from database
     * @param newOwnerEmail email address of new owner user
     * @return updated team in form of data transfer object
     */
    @PutMapping("team/{teamId}/teamOwner/{newOwnerEmail}")
    public TeamDTO changeTeamOwner(@PathVariable Long teamId, @PathVariable String newOwnerEmail) {
        RegisteredUser user = new RegisteredUser();
        Team team = new Team();
        try {
            user = userService.findUserByEmail(newOwnerEmail);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

        try {
            team = teamService.changeTeamOwner(teamId, user);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

        return TeamMapper.mapTeamToTeamDto(team);
    }


}
