/*
 * SubgroupTest
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.model.team;

import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for Subgroup class
 */
@RunWith(SpringRunner.class)
public class SubgroupTest {
    private Subgroup subgroup;
    private User user1;
    private User user2;

    /**
     * Before a test create a subgroup with one user.
     */
    @Before
    public void setUp() {
        List<User> userList = new ArrayList<>();

        user1 = new User("Ivan", "Stastny", "pass", "is@gmail.com");
        user2 = new User("Tomas", "Smutny", "pass2", "ts@gmail.com");
        userList.add(user1);
        subgroup = new Subgroup("Players", 0L);
        subgroup.setUserList(userList);
    }

    /**
     * Tests adding a user to the subgroup.
     */
    @Test
    public void addUser() {
        subgroup.addUser(user2);
        assertTrue(subgroup.getUserList().contains(user1));
        assertTrue(subgroup.getUserList().contains(user2));
    }

    /**
     * Tests removing a user form a subgroup.
     * @throws EntityNotFoundException if user to be removed wasn't found
     */
    @Test(expected = EntityNotFoundException.class)
    public void removeUser() throws EntityNotFoundException {
        subgroup.removeUser(user1);
        assertFalse(subgroup.getUserList().contains(user1));
        subgroup.removeUser(user2);
    }

    /**
     * Tests getting user from userList
     * @throws EntityNotFoundException if no user was found
     */
    @Test(expected = EntityNotFoundException.class)
    public void getUser() throws EntityNotFoundException {
        User regUs = subgroup.getUser(user1.getEmail());
        assertEquals(regUs, user1);
        subgroup.getUser(user2.getEmail());
    }

    /**
     * Tests if the user is in the subgroup or not.
     */
    @Test
    public void isUserInList() {
        assertTrue(subgroup.isUserInList(user1));
        assertFalse(subgroup.isUserInList(user2));
    }

}