/*
 * StubTeamRepository
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.repository.team;

import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.team.Subgroup;
import eu.profinit.stm.model.team.Team;
import eu.profinit.stm.repository.user.UserRepositoryStub;
import eu.profinit.stm.model.user.User;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Stub Team repository for Unit testing.
 */

@Repository
@Profile("stub_repository")
public class TeamRepositoryStub implements TeamRepository {

    private final Logger logger = Logger.getLogger(String.valueOf(getClass()));

    private static Team presetTeam;

    public static Team getPresetTeam() {

        setPresetTeam();

        return presetTeam;
    }

    private static void setPresetTeam() {
        User owner = UserRepositoryStub.loggedUser1;
        owner.setEntityId(1L);
        User presetUser = UserRepositoryStub.loggedUser5;
        presetUser.setEntityId(2L);
        Subgroup allUsersSubgroup = new Subgroup("All Users", 1L);
        allUsersSubgroup.addUser(owner);
        allUsersSubgroup.addUser(presetUser);
        Subgroup coachesSubgroup = new Subgroup("Coaches", 1L);
        coachesSubgroup.addUser(owner);
        Subgroup emptySubgroup = new Subgroup("Empty subgroup", 1L);
        List<Subgroup> subgroupList = new ArrayList<>();
        subgroupList.add(allUsersSubgroup);
        subgroupList.add(coachesSubgroup);
        subgroupList.add(emptySubgroup);
        presetTeam = new Team("B team", "sipky", subgroupList, owner);
        presetTeam.setEntityId(1L);
    }

    @Override
    public void insertTeam(Team team) {
        logger.info("STUB: Saving team!");
        team.setEntityId(10L);
    }

    @Override
    public void deleteTeam(Team team) throws EntityNotFoundException {
        logger.info("STUB: deleting team!");
        if (presetTeam == null) setPresetTeam();
        if (!team.getEntityId().equals(presetTeam.getEntityId())) throw new EntityNotFoundException("Team");
    }

    @Override
    public void updateTeam(Team team) throws EntityNotFoundException {
        logger.info("STUB: Updating team!");
        if (presetTeam == null) setPresetTeam();
        if (!team.getEntityId().equals(presetTeam.getEntityId())) throw new EntityNotFoundException("Team");
    }

    @Override
    public List<Team> findTeamsByName(String teamName) throws EntityNotFoundException {
        logger.info("STUB: Finding teams by name!");
        if (presetTeam == null) setPresetTeam();
        if (!teamName.equals(presetTeam.getName())) throw new EntityNotFoundException("Team");
        List<Team> teams = new ArrayList<>();
        teams.add(presetTeam);
        return teams;
    }

    @Override
    public Team findTeamById(Long teamId) throws EntityNotFoundException {
        logger.info("STUB: Finding team by id!");
        if (presetTeam == null) setPresetTeam();
        if (!teamId.equals(presetTeam.getEntityId())) throw new EntityNotFoundException("Team");
        return presetTeam;
    }
}
