/*
 * UserServiceImpl
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.service.user;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import eu.profinit.stm.crypto.Aes;
import eu.profinit.stm.dto.SignUpRequest;
import eu.profinit.stm.model.user.SocialProviderEnum;
import eu.profinit.stm.dto.user.LocalUser;
import eu.profinit.stm.exception.*;
import eu.profinit.stm.model.user.Guest;
import eu.profinit.stm.model.user.RoleEnum;
import eu.profinit.stm.repository.user.UserRepository;
import eu.profinit.stm.model.user.User;
import eu.profinit.stm.security.oauth2.user.OAuth2UserInfo;
import eu.profinit.stm.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * This implementation of the UserService controls the creation of the new user and checks if given logging
 * credentials corresponds to any user in database.
 */
@Service
@Profile("Main")
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    /**
     * Register a new user to the database. Before registration, checks if given email address is in the database.
     * Then encrypt a given password. Lastly, the new user is saved to the database.
     *
     * @param newUser newly registered user collected by controller
     * @return registered user
     * @throws EmailExistsException thrown, when given email is already in database
     */
    public User newUserRegistration(User newUser) throws EntityAlreadyExistsException {
        if (emailExists(newUser.getEmail())) {
            throw new EmailExistsException(
                    "Account with e-mail address " + newUser.getEmail() + "already exists.");
        }

        User user = new User();

        user.setName(newUser.getName());
        user.setSurname(newUser.getSurname());
        user.setEmail(newUser.getEmail());
        user.setRole(RoleEnum.USER);

        user.setPassword(passwordEncoder.encode(newUser.getPassword()));

        userRepository.insertUser(user);

        return user;
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

    /**
     * Checks if the given logging credentials match any user in the database. If not, throws an exception.
     *
     * @param userEmail logging email address
     * @param password  logging password
     * @return Logged user if successful
     * @throws UserOrPasswordNotMatchException if the logging credentials do not match any user in the database
     */
    public User userLogIn(String userEmail, String password) throws UserOrPasswordNotMatchException {
        User user = new User();

        try {
            user = userRepository.findUserByEmail(userEmail);
        } catch (Exception e) {
            if (e.getMessage().equals("User entity not found!")) {
                throw new UserOrPasswordNotMatchException();
            }
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new UserOrPasswordNotMatchException();
        }
    }

    /**
     * Gets a user form database sought by email.
     *
     * @param email user email
     * @return found user
     * @throws EntityNotFoundException thrown if user is not found in database
     */
    public User findUserByEmail(String email) throws EntityNotFoundException {
        return userRepository.findUserByEmail(email);
    }

    public User findUserById(Long id) throws EntityNotFoundException {
        return userRepository.findUserById(id);
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
        user.setName(newName);
        userRepository.updateUser(user);
        return user;
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
        user.setSurname(newSurname);
        userRepository.updateUser(user);
        return user;
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
            throw new EmailExistsException("Account with e-mail address " + newEmail + "already exists.");
        }
        user.setEmail(newEmail);
        userRepository.updateUser(user);
        return user;
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
        userRepository.updateUser(user);
        return user;
    }

    /**
     * Creates new Guest by AES encrypting (guest id + "-" + event id) and saves it to database.
     *
     * @param name    Guest name
     * @param eventId id of event for which we want to create an invitation
     * @return Guest that has been created
     * @throws EntityNotFoundException thrown when Event entity is not found
     */
    @Override
    public Guest createNewGuest(String name, Long eventId) throws EntityNotFoundException {
        //TODO maybe generate this
        String placeholderUri = "jsem_place_holder";

        userRepository.insertGuest(new Guest(name, placeholderUri));
        Guest guest = userRepository.findGuestByUri(placeholderUri);
        String uri = Aes.encrypt(guest.getEntityId() + "-" + eventId);

        guest.setUri(uri);
        userRepository.updateGuest(guest);

        return guest;
    }

    /**
     * Calls repository that will find guest by URI or throws exception
     *
     * @param uri identification of guest
     * @return desired Guest
     * @throws EntityNotFoundException thrown when Guest entity is not found
     */
    @Override
    public Guest findGuestByUri(String uri) throws EntityNotFoundException {
        return userRepository.findGuestByUri(uri);
    }

    @Override
    public User registerNewUser(final SignUpRequest signUpRequest) throws UserAlreadyExistAuthenticationException, EntityAlreadyExistsException {
        try {
            userRepository.findUserByEmail(signUpRequest.getEmail());
        } catch (EntityNotFoundException e) {
            User user = buildUser(signUpRequest);
            userRepository.insertUser(user);
            return user;
        }
        throw new UserAlreadyExistAuthenticationException("User with email " + signUpRequest.getEmail() + " already exist");
    }

    private User buildUser(final SignUpRequest formDTO) {
        User user = new User();
        user.setName(formDTO.getName());
        user.setSurname(formDTO.getSurname());
        user.setEmail(formDTO.getEmail());
        user.setPassword(passwordEncoder.encode(formDTO.getPassword()));
        user.setRole(RoleEnum.USER);
        user.setSocialProvider(formDTO.getSocialProvider());
        //user.setEnabled(true);
        //user.setProviderUserId(formDTO.getProviderUserId());
        return user;
    }

    @Override
    public LocalUser processUserRegistration(SocialProviderEnum socialProvider, Map<String, Object> attributes, OidcIdToken idToken, OidcUserInfo userInfo) throws EntityAlreadyExistsException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialProvider, attributes);
        if (StringUtils.isBlank(oAuth2UserInfo.getName())) {
            throw new OAuth2AuthenticationProcessingException("Name not found from OAuth2 provider");
        } else if (StringUtils.isBlank(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        SignUpRequest userDetails = toUserRegistrationObject(socialProvider, oAuth2UserInfo);
        User user;
        try {
            user = findUserByEmail(oAuth2UserInfo.getEmail());
            if (!socialProvider.equals(user.getSocialProvider()) && !SocialProviderEnum.LOCAL.equals(user.getSocialProvider())) {
                throw new OAuth2AuthenticationProcessingException(
                        "Looks like you're signed up with " + user.getSocialProvider() + " account. Please use your " + user.getSocialProvider() + " account to login.");
            }
            updateExistingUser(user, oAuth2UserInfo);
        } catch (EntityNotFoundException e) {
            user = registerNewUser(userDetails);
        }
        return LocalUser.create(user, attributes, idToken, userInfo);
    }

    @SneakyThrows
    private void updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName().split("\\s+", 2)[0]);
        userRepository.updateUser(existingUser);
    }

    private SignUpRequest toUserRegistrationObject(SocialProviderEnum socialProvider, OAuth2UserInfo oAuth2UserInfo) {
        String[] nameParts = oAuth2UserInfo.getName().split("\\s+", 2);
        return SignUpRequest.getBuilder().addProviderUserID(oAuth2UserInfo.getId()).addName(nameParts[0]).addSurname(nameParts[1]).addEmail(oAuth2UserInfo.getEmail())
                .addSocialProvider(socialProvider).addPassword("changeit").build(); // TODO change it
    }
}
