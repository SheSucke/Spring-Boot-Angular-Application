/*
 * UserController
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.controller.user;

import eu.profinit.stm.configuration.CurrentUser;
import eu.profinit.stm.dto.user.LocalUser;
import eu.profinit.stm.dto.user.UserDetailsDto;
import eu.profinit.stm.dto.user.UserDto;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.exception.HttpExceptionHandler;
import eu.profinit.stm.mapper.UserMapper;
import eu.profinit.stm.model.user.User;
import eu.profinit.stm.oauth.PrincipalExtractorImpl;
import eu.profinit.stm.service.user.AuthenticationFacade;
import eu.profinit.stm.service.user.UserService;
import eu.profinit.stm.util.GeneralUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Maps incoming user requests.
 * TODO not all features implemented
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    //@Autowired
    private DaoAuthenticationProvider authenticationProvider;
    @Autowired
    private PrincipalExtractorImpl principalExtractor;
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser(@CurrentUser @NonNull LocalUser user) {
        log.debug("endpoint /user/me hit!");
        return ResponseEntity.ok(GeneralUtils.buildUserInfo(user));
    }

    /**
     * Provides a new user registration with UserDetailsDTO. Checks if user email is not already in database.
     * If is, throws BAD_REQUEST exception.
     *
     * @param newUser UserDetails data transfer object containing email and password
     * @param request http request
     */
    @PostMapping("/user/registration")
    public void registerNewUser(@RequestBody UserDetailsDto newUser, HttpServletRequest request) {
        User user = UserMapper.mapUserDetailsDTOToUser(newUser);
        try {
            userService.newUserRegistration(user);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }

    }

    /**
     * Gets registered user from database by email.
     *
     * @param userEmail email address of seek user
     * @return registered user data transfer object
     */
    @GetMapping("/user/{userEmail}")
    public UserDto refreshUser(@PathVariable String userEmail) {
        User user = new User();
        try {
            user = userService.findUserByEmail(userEmail);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
        return UserMapper.mapUserToUserDTO(user);
    }

    /**
     * User login
     *
     * @param user    UserDetails data transfer object containing email and password
     * @param request http request
     * @deprecated Maybe will be used later.
     */
    @Deprecated
    @PostMapping("/login")
    public void userLogin(@RequestBody UserDetailsDto user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                user.getName(), user.getPassword(), new ArrayList<>());
        token.setDetails(new WebAuthenticationDetails(request));
        SecurityContext context = SecurityContextHolder.getContext();
        try {
            final Authentication auth = authenticationProvider.authenticate(token);
            context.setAuthentication(auth);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }

        Authentication auth = context.getAuthentication();
        auth.getPrincipal();
    }

    /**
     * Changes name of the user.
     *
     * @param userEmail   user what name should be changed
     * @param userNewName new name of the user
     * @return user DTO with updated name
     */
    @PutMapping("user/{userEmail}/name/{userNewName}")
    public UserDto changeUserName(@PathVariable String userEmail, @PathVariable String userNewName) {
        User user = new User();
        try {
            user = userService.changeUserName(userEmail, userNewName);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
        return UserMapper.mapUserToUserDTO(user);
    }

    /**
     * Changes surname of the user.
     *
     * @param userEmail      user what surname should be changed
     * @param userNewSurname new surname of the user
     * @return user DTO with updated surname
     */
    @PutMapping("user/{userEmail}/surname/{userNewSurname}")
    public UserDto changeUserSurname(@PathVariable String userEmail, @PathVariable String userNewSurname) {
        User user = new User();
        try {
            user = userService.changeUserSurname(userEmail, userNewSurname);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
        return UserMapper.mapUserToUserDTO(user);
    }

    /**
     * Changes email of the user.
     *
     * @param userEmail    user what surname should be changed
     * @param userNewEmail new surname of the user
     * @return user DTO with updated surname
     */
    @PutMapping("user/{userEmail}/email/{userNewEmail}")
    public UserDto changeUserEmail(@PathVariable String userEmail, @PathVariable String userNewEmail) {
        User user = new User();
        try {
            user = userService.changeUserEmail(userEmail, userNewEmail);
        } catch (Exception e) {
            HttpExceptionHandler.httpErrorMessages(e);
        }
        return UserMapper.mapUserToUserDTO(user);
    }

    /**
     * Maps a logging success message if user is already registered. Registration message if user is registering for a first time.
     *
     * @return logging message
     */
    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        String email = principalExtractor.getPrincipalEmail();
        Authentication authentication = authenticationFacade.getAuthentication();
        try {
            userService.findUserByEmail(email);
        } catch (Exception e) {
            if (e.getMessage().equals("User entity not found!")) {
                User user = new User(
                        principalExtractor.getPrincipalNameAndSurname()[0],
                        principalExtractor.getPrincipalNameAndSurname()[1],
                        "",
                        email);
                try {
                    userService.newUserRegistration(user);
                } catch (EntityNotFoundException | EntityAlreadyExistsException ex) {
                    HttpExceptionHandler.httpErrorMessages(ex);
                }
                return "Registration successful";
            }
        }
        return "Welcome back";
    }

    /**
     * Maps a logout success message.
     *
     * @return logout message
     */
    @GetMapping("/logoutSuccess")
    public String logoutSuccess() {
        return "Logout successful";
    }
}
