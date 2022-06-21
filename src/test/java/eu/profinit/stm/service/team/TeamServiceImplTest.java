/*
 * TeamServiceImplTest
 *
 * 0.1
 *
 * Author: J. Janský
 */

package eu.profinit.stm.service.team;

import eu.profinit.stm.configuration.StubRepositoryConfiguration;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.team.Team;
import eu.profinit.stm.repository.subgroup.SubgroupRepository;
import eu.profinit.stm.repository.team.TeamRepository;
import eu.profinit.stm.repository.team.TeamRepositoryStub;
import eu.profinit.stm.repository.user.UserRepository;
import eu.profinit.stm.model.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


/**
 * Unit tests for Team service implementation
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { StubRepositoryConfiguration.class})
@ActiveProfiles({"stub_repository"})
public class TeamServiceImplTest {

    private final String ALL_USER_SUBGROUP = "All Users";
    private final String COACHES_SUBGROUP = "Coaches";
    private User presetUser;
    private Team presetTeam;
    private User dummyUser;

    private TeamServiceImpl teamService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SubgroupRepository subgroupRepository;

    /**
     * Before a test create new TeamServiceImpl using stub repositories, TeamDTO and new user.
     */
    @Before
    public void setUp() {
        teamService = new TeamServiceImpl(teamRepository, userRepository,subgroupRepository);

        presetTeam = TeamRepositoryStub.getPresetTeam();
        presetUser = presetTeam.getOwner();

        dummyUser = new User(
                "Honza",
                "Kral",
                "asd",
                "a@b.c");
        dummyUser.setEntityId(99L);
    }

    /**
     * Tests creation of a new team
     */
    @Test
    public void createNewTeam() {
        Team newTeam = teamService.createNewTeam(presetTeam);
        Assert.assertEquals(presetUser, newTeam.getOwner());
        assertEquals(presetTeam.getName(), newTeam.getName());
        assertEquals(presetTeam.getSport(), newTeam.getSport());
        Assert.assertEquals(presetTeam.getOwner(), newTeam.getOwner());
        assertEquals(presetTeam.getSubgroupList(), newTeam.getSubgroupList());
    }

    /**
     * Get existing team by id test
     */
    @Test
    public void getsTeam() throws EntityNotFoundException {
        Team team = teamService.getTeamById(1L);
        assertNotNull(team);
    }

    /**
     * Tests creation of getting non-existing team
     */
    @Test(expected = EntityNotFoundException.class)
    public void getsNonExistentTeam() throws EntityNotFoundException {
        teamService.getTeamById(0L);
    }

    /**
     * Tests of adding a new subgroup to the specific team.
     */
    @Test(expected = EntityAlreadyExistsException.class)
    public void addNewSubgroup() throws EntityNotFoundException, EntityAlreadyExistsException {
        String subgroupName = "Players";
        Team team = teamService.addSubgroup(presetTeam.getEntityId(), subgroupName);
        assertEquals(4, team.getSubgroupList().size());
        assertEquals(subgroupName, team.getSubgroupList().get(3).getName());
        teamService.addSubgroup(team.getEntityId(), subgroupName);
    }

    /**
     * Tests removing a subgroup from a team
     */
    @Test(expected = EntityNotFoundException.class)
    public void deleteSubgroup() throws EntityNotFoundException {
        String subgroupName = "Empty subgroup";
        teamService.deleteSubgroup(presetTeam.getEntityId(), subgroupName);
        assertEquals(2, presetTeam.getSubgroupList().size());
        teamService.deleteSubgroup(presetTeam.getEntityId(), subgroupName);
    }

    /**
     * Tests adding new user to the team.
     */
    @Test
    public void addUserToTeam() throws EntityNotFoundException, EntityAlreadyExistsException {
        User user = new User("Tomas", "Smutny", "pass2", "ts@gmail.com");
        teamService.addUserToTeam(presetTeam.getEntityId(), user);
        assertTrue(presetTeam.getTeamSubgroup(ALL_USER_SUBGROUP).isUserInList(user));
    }

    /**
     * Tests adding new user to the subgroup which is not in already in a team.
     */
    @Test
    public void addUserToSubgroupWhoIsNotInAllUsers() throws EntityNotFoundException, EntityAlreadyExistsException {
        User user = new User("Tomas", "Smutny", "pass2", "ts@gmail.com");
        teamService.addUserToSubgroup(presetTeam.getEntityId(), "Coaches", user);
        assertTrue(presetTeam.getTeamSubgroup(COACHES_SUBGROUP).isUserInList(user));
        assertTrue(presetTeam.getTeamSubgroup(ALL_USER_SUBGROUP).isUserInList(user));
    }

    /**
     * Tests adding a new user to the subgroup.
     */
    @Test
    public void addUserToSubgroupWhoIsInAllUsers() throws EntityNotFoundException, EntityAlreadyExistsException {
        String emptySubgroupName = "Empty subgroup";
        teamService.addUserToSubgroup(presetTeam.getEntityId(), emptySubgroupName, presetUser);
        assertEquals(presetUser, presetTeam.getTeamSubgroup(emptySubgroupName).getUserList().get(0));
        assertEquals(2, presetTeam.getTeamSubgroup(ALL_USER_SUBGROUP).getUserList().size());
    }


    /**
     * Tests adding a user to the subgroup who is already in a subgroup.
     */
    @Test(expected = EntityAlreadyExistsException.class)
    public void addAlreadyAddedUser() throws EntityAlreadyExistsException, EntityNotFoundException {
        teamService.addUserToSubgroup(presetTeam.getEntityId(), COACHES_SUBGROUP, presetUser);
    }

    /**
     * Tests deleting a user from a team and all subgroups.
     */
    @Test
    public void deleteUser() throws EntityNotFoundException {
        teamService.deleteUserFromTeam(presetTeam.getEntityId(), presetUser);
        assertFalse(presetTeam.getTeamSubgroup(ALL_USER_SUBGROUP).isUserInList(presetUser));
        assertFalse(presetTeam.getTeamSubgroup(COACHES_SUBGROUP).isUserInList(presetUser));
    }

    /**
     * Tests deleting a user from a subgroup.
     */
    @Test
    public void deleteUserFromSubgroup() throws EntityNotFoundException {
        teamService.deleteUserFromSubgroup(presetTeam.getEntityId(), COACHES_SUBGROUP, presetUser);
        assertFalse(presetTeam.getTeamSubgroup(COACHES_SUBGROUP).isUserInList(presetUser));
    }


    /**
     * Tests changing a team name.
     */
    @Test
    public void changeTeamName() throws EntityNotFoundException {
        String newName = "C team";
        presetTeam = teamService.changeTeamName(presetTeam.getEntityId(), newName);
        assertEquals(newName, presetTeam.getName());
    }

    /**
     * Tests a changing a team sport
     */
    @Test
    public void changeTeamSport() throws EntityNotFoundException {
        String sportTeam = "Rugby";
        presetTeam = teamService.changeTeamSport(presetTeam.getEntityId(), sportTeam);
        assertEquals(sportTeam, presetTeam.getSport());
    }

    /**
     * Tests a changing the owner
     */
    @Test(expected = EntityNotFoundException.class)
    public void changeTeamOwner() throws EntityNotFoundException, EntityAlreadyExistsException {
        teamService.changeTeamOwner(presetTeam.getEntityId(), dummyUser);
        Assert.assertEquals(dummyUser, presetTeam.getOwner());
        assertTrue(presetTeam.getTeamSubgroup(COACHES_SUBGROUP).isUserInList(dummyUser));

    }

    /**
     * Tests a changing the owner who is not in team
     */
    @Test(expected = EntityNotFoundException.class)
    public void changeTeamOwnerWhoIsNotInAllUsers() throws EntityNotFoundException, EntityAlreadyExistsException {
        teamService.changeTeamOwner(presetTeam.getEntityId(), dummyUser);
    }


    /**
     * Tests a changing the owner who is not in team
     */
    @Test(expected = EntityNotFoundException.class)
    public void changeTeamOwnerWhoIsNotInTeam() throws EntityNotFoundException, EntityAlreadyExistsException {
        teamService.changeTeamOwner(presetTeam.getEntityId(), dummyUser);
    }

    /**
     * Tests a changing the owner who is not in coaches
     */
    @Test
    public void changeTeamOwnerWhoIsNotInCoaches() throws EntityNotFoundException, EntityAlreadyExistsException {
        User user = presetTeam.getTeamSubgroup(ALL_USER_SUBGROUP).getUser("email@gmail.com");
        teamService.changeTeamOwner(presetTeam.getEntityId(), user);
        assertTrue(presetTeam.getTeamSubgroup(COACHES_SUBGROUP).isUserInList(user));
        Assert.assertEquals(user, presetTeam.getOwner());
    }

    /**
     * Tests a changing a subgroup name
     */
    @Test
    public void changeSubgroupName() throws EntityNotFoundException, EntityAlreadyExistsException {
        String newSubgroupName = "Players";
        teamService.changeSubgroupName(presetTeam.getEntityId(), COACHES_SUBGROUP, newSubgroupName);
        assertEquals(newSubgroupName, presetTeam.getTeamSubgroup(newSubgroupName).getName());
        assertFalse(presetTeam.isSubgroupInTeam(COACHES_SUBGROUP));
    }

    /**
     * Tests a changing a subgroup name to the name of already existing one
     */
    @Test(expected = EntityAlreadyExistsException.class)
    public void changeSubgroupNameToAlreadyExistingOne() throws EntityNotFoundException, EntityAlreadyExistsException {
        teamService.changeSubgroupName(presetTeam.getEntityId(), COACHES_SUBGROUP, ALL_USER_SUBGROUP);
    }

    /**
     * Tests a changing a subgroup name which do not exist in team
     */
    @Test(expected = EntityNotFoundException.class)
    public void changeSubgroupNameOfNotExistentSubgroup() throws EntityNotFoundException, EntityAlreadyExistsException {
        teamService.changeSubgroupName(presetTeam.getEntityId(), "Tigers", "Lions");
    }

}