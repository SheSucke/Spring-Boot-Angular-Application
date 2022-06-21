/*
 * UserServiceImplTest
 *
 * 0.1
 *
 * Author: J. Janský
 */

package eu.profinit.stm.service.user;

import eu.profinit.stm.configuration.StubRepositoryConfiguration;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.model.user.Guest;
import eu.profinit.stm.model.user.RoleEnum;
import eu.profinit.stm.repository.user.UserRepository;
import eu.profinit.stm.repository.user.UserRepositoryStub;
import eu.profinit.stm.model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Unit tests for User service implementation
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = StubRepositoryConfiguration.class)
@ActiveProfiles({"stub_repository"})
public class UserServiceImplTest {
    private UserServiceImpl userService;
    private User user;
    private User user2;
    private Guest guestDetails;

    private static final String key = "AES";

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Before a test create new UserServiceImpl using stub repositories and create two stub users.
     */
    @Before
    public void setUp() {
        UserRepository userRepository = new UserRepositoryStub();

        userService = new UserServiceImpl(passwordEncoder, userRepository);
        user = new User("Ivan", "Stastny", "pass", "is@gmail.com");
        user2 = new User("Tomas", "Smutny", "pass", "ab@gmail.com");
        guestDetails = new Guest("Tom", "UG1qimsuz2RsW5MrAQM0Wg==");
    }

    /**
     * Tests a successful registration of the new user.
     */
    @Test
    public void newUserRegistration() throws EntityAlreadyExistsException {
        User newUser = userService.newUserRegistration(user2);
        assertEquals(user2.getName(), newUser.getName());
        assertEquals(user2.getSurname(), newUser.getSurname());
        assertEquals(user2.getEmail(), newUser.getEmail());
        assertEquals(user2.getRole(), newUser.getRole());
        assertNotEquals(user2.getPassword(), newUser.getPassword());
    }

    /**
     * Tests unsuccessful registration of already registered user.
     */
    @Test
    public void registrationOfExistingUser() {
        try {
            user = userService.newUserRegistration(user);
        } catch (Exception e) {
            assertEquals("Account with e-mail address " + user.getEmail() + "already exists.", e.getMessage());
        }
    }


    /**
     * Tests a successful user logging.
     */
    @Test
    public void userLogInSuccess() {
        User user = userService.userLogIn("is@gmail.com", "pass");
        assertEquals("is@gmail.com", user.getEmail());
        assertEquals("$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", user.getPassword());
        assertEquals("Stastny", user.getSurname());
        assertEquals("Ivan", user.getName());
        assertEquals(RoleEnum.USER, user.getRole());
    }

    /**
     * Tests an unsuccessful logging of non-existent user.
     */
    @Test
    public void userLogInNotExistingUser() {
        try {
            User user = userService.userLogIn("is@gmal.com", "pass");
            assertNull(user);
        } catch (Exception e) {
            assertEquals("User and password do not match", e.getMessage());
        }
    }

    /**
     * Tests an unsuccessful logging of existent user with .
     */
    @Test
    public void userLogInBadPassword() {
        try {
            User user = userService.userLogIn("is@gmail.com", "pass24");
            assertNull(user);
        } catch (Exception e) {
            assertEquals("User and password do not match", e.getMessage());
        }
    }

    /**
     * Testing creation of a new Guest invitation. Positive ending
     * @throws EntityNotFoundException Thrown when event is not found
     */
    @Test
    public void createNewGuestCreatesNewGuest() throws EntityNotFoundException {
        Guest guest = userService.createNewGuest(guestDetails.getName(), 0L);
        assertEquals(guestDetails.getName(), guest.getName());
        assertEquals(guestDetails.getRole(), guest.getRole());
        assertEquals(guestDetails.getUri(), guest.getUri());
    }

    /**
     * Testing findGuestByUri finds guest by given URI. Positive ending
     * @throws EntityNotFoundException thrown when Guest is not found
     */
    @Test
    public void findGuestByUriFindsGuestByUri() throws EntityNotFoundException {
       Guest guest =  userService.findGuestByUri(guestDetails.getUri());
        assertEquals(guestDetails.getName(), guest.getName());
        assertEquals(guestDetails.getRole(), guest.getRole());
        assertEquals(guestDetails.getUri(), guest.getUri());
    }

    /**
     * Testing findGuestBuUri throws exception when Guest is not found
     */
    @Test
    public void findGuestByUriThrownEntityNotFoundExceptionForNonExistentGuest() {
        try {
            userService.findGuestByUri(guestDetails.getUri());
        } catch (EntityNotFoundException e) {
            assertEquals("Guest entity not found!", e.getMessage());
        }
    }
}