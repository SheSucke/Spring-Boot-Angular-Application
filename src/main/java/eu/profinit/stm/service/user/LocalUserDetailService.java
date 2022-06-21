package eu.profinit.stm.service.user;

import eu.profinit.stm.dto.user.LocalUser;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.exception.ResourceNotFoundException;
import eu.profinit.stm.model.user.User;
import eu.profinit.stm.util.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Chinna
 */
@Service("localUserDetailService")
public class LocalUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public LocalUser loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userService.findUserByEmail(username);
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
        return createLocalUser(user);
    }

    public LocalUser loadUserByEmail(String email) {
        try {
            return createLocalUser(userService.findUserByEmail(email));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("User", "email", email);
        }
    }

    public LocalUser loadUserById(Long id) {
        try {
            return createLocalUser(userService.findUserById(id));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("User", "id", id);
        }
    }

    private LocalUser createLocalUser(User user) {
        return new LocalUser(user.getEmail(), user.getPassword(), true, true, true, GeneralUtils.buildSimpleGrantedAuthorities(Set.of(user.getRole())), user);
    }
}
