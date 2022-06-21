/*
 * AuthenticationFacadeImpl
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.service.user;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Used only to obtain authentication token form SecurityContextHolder.
 */
@Service
@Profile({"main","aspect","authentication"})
@NoArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    /**
     * Gets authentication token from SecurityContextHolder
     *
     * @return authentication token
     */
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
