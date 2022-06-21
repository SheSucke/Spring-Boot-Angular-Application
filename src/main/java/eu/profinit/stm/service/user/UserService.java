/*
 * UserService
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.service.user;

import eu.profinit.stm.dto.SignUpRequest;
import eu.profinit.stm.model.user.SocialProviderEnum;
import eu.profinit.stm.dto.user.LocalUser;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.exception.UserAlreadyExistAuthenticationException;
import eu.profinit.stm.model.user.Guest;
import eu.profinit.stm.model.user.RoleEnum;
import eu.profinit.stm.model.user.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;

/**
 * The UserService controls the creation of the new user and checks if given logging
 * credentials corresponds to any user in database.
 */
public interface UserService {
    User newUserRegistration(User newUser) throws EntityNotFoundException, EntityAlreadyExistsException;

    User findUserByEmail(String email) throws EntityNotFoundException;

    User findUserById(Long id) throws EntityNotFoundException;

    User changeUserName(String email, String newName) throws EntityNotFoundException;
    User changeUserSurname(String email, String newSurname) throws EntityNotFoundException;
    User changeUserEmail(String email, String newEmail) throws EntityNotFoundException;
    User changeUserRole(String email, RoleEnum newRole) throws EntityNotFoundException;

    Guest createNewGuest(String name, Long eventId) throws EntityNotFoundException;
    Guest findGuestByUri(String uri) throws EntityNotFoundException;

    User registerNewUser(SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException, EntityAlreadyExistsException;

    LocalUser processUserRegistration(SocialProviderEnum socialProvider, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) throws EntityNotFoundException, EntityAlreadyExistsException;

    //Optional<User> findUserById(Long id);
}
