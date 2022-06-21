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
 * Implementation of StubUserDetailsService from spring security for DaoAuthenticationProvider.
 */
@Service
@Profile({"stub_services","authentication"})
@AllArgsConstructor
public class UserDetailsServiceStub implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    /**

    /**
     * Gets stubbed user details. User is defined by email address.
     *
     * @param userEmail email address of user
     * @return user details containing user email address and password
     * @throws UsernameNotFoundException thrown when user is not in database
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = new User();
        if (userEmail.equals("is@seznam.cz") ||
                userEmail.equals("is@gmail.com") ||
                userEmail.equals("is@email.cz") ||
                userEmail.equals("ts@gmail.com") ||
                userEmail.equals("email@gmail.com")) {
            return new UserDetailsImpl(userEmail, "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2");
        } else {
            throw new UserOrPasswordNotMatchException();
        }
    }
}

