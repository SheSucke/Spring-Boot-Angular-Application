/*
 * UserDetailServiceImpl
 *
 * 0.1
 *
 * Author: J. Janský
 */

package eu.profinit.stm.service.user;

import eu.profinit.stm.exception.UserOrPasswordNotMatchException;
import eu.profinit.stm.repository.user.UserRepository;
import eu.profinit.stm.model.user.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * Implementation of UserDetailsService from spring security for DaoAuthenticationProvider.
 */
@Service
@Profile({"stub", "Main"})
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Loads a user email and password from a database. User is defined by email address.
     *
     * @param userEmail email address of user
     * @return user details containing user email address and password
     * @throws UsernameNotFoundException thrown when user is not in database
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = new User();

        try {
            user = userRepository.findUserByEmail(userEmail);
        } catch (Exception e) {
            if (e.getMessage().equals("User entity not found!")) {
                throw new UserOrPasswordNotMatchException();
            }
        }

        return new UserDetailsImpl(user.getEmail(), user.getPassword());
    }
}
