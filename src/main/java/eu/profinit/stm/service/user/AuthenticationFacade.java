/*
 * AuthenticationFacade
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.service.user;

import org.springframework.security.core.Authentication;


/**
 * Interface used for getting authentication token.
 */
public interface AuthenticationFacade {


    /**
     * Gets authentication token.
     *
     * @return authentication token
     */
    Authentication getAuthentication();
}
