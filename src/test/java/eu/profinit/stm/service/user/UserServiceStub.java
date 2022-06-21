/*
 * StubUserServiceImpl
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.service.user;

import eu.profinit.stm.crypto.Aes;
import eu.profinit.stm.dto.SignUpRequest;
import eu.profinit.stm.model.user.SocialProviderEnum;
import eu.profinit.stm.dto.user.LocalUser;
import eu.profinit.stm.exception.EmailExistsException;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.exception.UserAlreadyExistAuthenticationException;
import eu.profinit.stm.model.user.Guest;
import eu.profinit.stm.model.user.RoleEnum;
import eu.profinit.stm.model.user.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


/**
 * Stub User service for Unit tests.
 */
@Service
public class UserServiceStub implements UserService {

    private final User loggedUser1 = new User("Ivan", "Stastny", "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "is@gmail.com");
    private final User loggedUser2 = new User("Pavel", "Smutny", "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "is@seznam.cz");
    private final User loggedUser3 = new User("Jirka", "Vesely", "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "is@email.cz");
    private final User loggedUser4 = new User("Tomas", "Smutny", "pass2", "ts@gmail.com");
    private final User loggedUser5 = new User("Adam", "Stastny", "2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "email@gmail.com");

    private final Guest guest = new Guest("Karel","mxPR4fbWzvai60UMLhD3aw==");


    /**
     * Registers a new user.
     * If a new user email address is equal to "email@gmail.com",
     * the user is already in database and method throws EmailExistsException.
     *
     * @param newUser new user
     * @return registered user
     * @throws EmailExistsException thrown if new user email is equal to "email@gmail.com"
     */
    @Override
    public User newUserRegistration(User newUser) throws EmailExistsException {

        if (newUser.getEmail().equals("email@gmail.com")) {
            throw new EmailExistsException(
                    "Account with e-mail address " + newUser.getEmail() + "already exists.");
        }

        User user = new User();
        user.setName(newUser.getName());
        user.setSurname(newUser.getSurname());
        user.setEmail(newUser.getEmail());
        user.setRole(RoleEnum.USER);

        user.setPassword("hashedPassword");
        return user;
    }

    /**
     * /TODO badly implemented feature
     *
     * @return
     */
    public User getLogedUser() {
        return new User("Adam", "Stastny", "pass", "email@gmail.com");
    }

    /**
     * Changes user name.
     *
     * @param email   email address of user to find him in database
     * @param newName new user name
     * @return user with changed name
     * @throws EntityNotFoundException if user is not found
     */
    @Override
    public User changeUserName(String email, String newName) throws EntityNotFoundException {
        User user = findUserByEmail(email);
        User userOut = new User(
                user.getName(),
                user.getSurname(),
                user.getPassword(),
                user.getEmail());
        userOut.setName(newName);
        return userOut;
    }

    /**
     * Changes user surname
     *
     * @param email      email address of user to find him in database
     * @param newSurname new user surname
     * @return user with changed surname
     * @throws EntityNotFoundException if user is not found
     */
    @Override
    public User changeUserSurname(String email, String newSurname) throws EntityNotFoundException {
        User user = findUserByEmail(email);
        User userOut = new User(
                user.getName(),
                user.getSurname(),
                user.getPassword(),
                user.getEmail());
        userOut.setSurname(newSurname);
        return userOut;
    }

    /**
     * Changes user email. Before checks if email is not already taken.
     *
     * @param email    email address of user to find him in database
     * @param newEmail new email address
     * @return user with changed email address
     * @throws EntityNotFoundException if user is not found
     */
    @Override
    public User changeUserEmail(String email, String newEmail) throws EntityNotFoundException {
        User user = findUserByEmail(email);
        if (emailExists(newEmail)) {
            throw new EmailExistsException("Account with e-mail address " + newEmail + " already exists.");
        }
        User userOut = new User(
                user.getName(),
                user.getSurname(),
                user.getPassword(),
                user.getEmail());
        userOut.setEmail(newEmail);
        return userOut;
    }


    /**
     * Changes user role.
     *
     * @param email   email address of user to find him in database
     * @param newRole new user role
     * @return user with changed user role
     * @throws EntityNotFoundException if user is not found
     */
    @Override
    public User changeUserRole(String email, RoleEnum newRole) throws EntityNotFoundException {
        User user = findUserByEmail(email);
        user.setRole(newRole);
        return user;
    }

    /**
     * Creates guest with id OL
     * @param name name of Guest
     * @param eventId id of event to which is user invited
     * @return created Guest
     */
    @Override
    public Guest createNewGuest(String name, Long eventId) {
        Guest guest = new Guest(name,"placeholderUri");
        guest.setEntityId(0L);
        String uri = Aes.encrypt(guest.getEntityId() + "-" + eventId);
        guest.setUri(uri);

        return guest;
    }

    /**
     * Returns guest for matching URI or throws EntityNotFoundException
     * @param uri URI which user should have
     * @return dummy guest
     * @throws EntityNotFoundException thrown when Guest is not found
     */
    @Override
    public Guest findGuestByUri(String uri) throws EntityNotFoundException {
        if (uri.equals("mxPR4fbWzvai60UMLhD3aw==")) {
            return guest;
        } else if (uri.equals("jsem_place_holder")) {
            return guest;
        } else {
            throw new EntityNotFoundException("Guest");
        }
    }

    @Override
    public User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException, EntityAlreadyExistsException {
        return null;
    }

    @Override
    public LocalUser processUserRegistration(SocialProviderEnum socialProvider, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) throws EntityNotFoundException, EntityAlreadyExistsException {
        return null;
    }

    /**
     * Returns STUB user found by email. Two possible users with email "email@gmail.com" or "ts@gmail.com" are returned.
     * Otherwise, the exception EntityNotFoundException is thrown.
     *
     * @param email user email
     * @return a STUB user
     * @throws EntityNotFoundException thrown if new user email is not equal to "email@gmail.com" or "ts@gmail.com"
     */
    @Override
    public User findUserByEmail(String email) throws EntityNotFoundException {
        User user = new User();
        if (Objects.equals(email, "is@seznam.cz")) {
            return loggedUser2;
        } else if (email.equals("is@gmail.com")) {
            return loggedUser1;
        } else if (email.equals("is@email.cz")) {
            return loggedUser3;
        } else if (email.equals("ts@gmail.com")) {
            return loggedUser4;
        } else if (email.equals("email@gmail.com")) {
            return loggedUser5;
        } else {
            throw new EntityNotFoundException("User");
        }
    }

    @Override
    public User findUserById(Long id) throws EntityNotFoundException {
        return null;
    }

    /**
     * Finds if user exists in database by email.
     *
     * @param email user email
     * @return true if user is in database, false otherwise
     */
    private boolean emailExists(String email) {
        try {
            findUserByEmail(email);
        } catch (Exception e) {
            if (e.getMessage().equals("User entity not found!")) {
                return false;
            }
        }
        return true;
    }
}
