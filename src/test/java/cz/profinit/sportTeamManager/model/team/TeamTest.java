/*
 * TeamTest
 *
 * 0.1
 *
 * Author: J. Janský
 */
package cz.profinit.sportTeamManager.model.team;

import cz.profinit.sportTeamManager.configuration.StubRepositoryConfiguration;
import cz.profinit.sportTeamManager.exceptions.EntityNotFoundException;
import cz.profinit.sportTeamManager.model.user.RegisteredUser;
import cz.profinit.sportTeamManager.model.user.RoleEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for Team class
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = StubRepositoryConfiguration.class)
public class TeamTest {
    private Team team;
    private Subgroup subgroup;

    /**
     * Before a test create a team with one subgroup and owner.
     */
    @Before
    public void setUp() {
        List<RegisteredUser> userList = new ArrayList<>();
        List<Subgroup> subgroupList = new ArrayList<>();

        RegisteredUser user1 = new RegisteredUser("Ivan", "Stastny", "pass", "is@gmail.com", RoleEnum.USER);
        userList.add(user1);
        team = new Team("A team", "Vodní lyžování", subgroupList, user1);
        subgroup = new Subgroup("Players", team.getEntityId());
        subgroup.setUserList(userList);
        subgroupList.add(subgroup);
        team.setSubgroupList(subgroupList);
    }

    /**
     * Tests adding a new subgroup to the team.
     */
    @Test
    public void addNewSubgroup() {
        team.addNewSubgroup("Beginners");
        assertEquals(2, team.getSubgroupList().size());
        assertEquals("Beginners", team.getSubgroupList().get(1).getName());
    }

    /**
     * Tests obtaining a specific subgroup from a team.
     */
    @Test
    public void getTeamSubgroup() throws EntityNotFoundException {
        assertEquals(subgroup, team.getTeamSubgroup("Players"));
        assertThrows(EntityNotFoundException.class, () -> team.getTeamSubgroup("A"));
    }

    /**
     * Tests deleting a specific subgroup from a team.
     */
    @Test
    public void deleteSubgroup() throws EntityNotFoundException {
        assertEquals(1, team.getSubgroupList().size());
        team.deleteSubgroup("Players");
        assertEquals(0, team.getSubgroupList().size());
        assertThrows(EntityNotFoundException.class, () -> team.deleteSubgroup("A"));
    }

    /**
     * Tests if the subgroup is in team or not.
     */
    @Test
    public void isSubgroupInTeam() {
        assertTrue(team.isSubgroupInTeam("Players"));
        assertFalse(team.isSubgroupInTeam("Nic"));
    }
}