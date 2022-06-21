/*
 * AuthenticationConfiguration
 *
 * 0.1
 *
 * Author: J. Jansky
 */

package eu.profinit.stm.configuration;

import eu.profinit.stm.oauth.PrincipalExtractorImpl;
import eu.profinit.stm.repository.user.UserRepository;
import eu.profinit.stm.service.user.AuthenticationFacade;
import eu.profinit.stm.service.user.AuthenticationFacadeImpl;
import eu.profinit.stm.service.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration bringing classes necessary for Authentication.
 */
@Configuration
@Import(PasswordEncoderConfiguration.class)
@ComponentScan(basePackageClasses = AuthenticationFacade.class)
@Profile("Main")
public class AuthenticationConfiguration {
/*

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        UserDetailsService userDetailsService = new UserDetailsServiceImpl(userRepository); // TODO remove arg
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    PrincipalExtractorImpl principalExtractor() {
        return new PrincipalExtractorImpl();
    }
*/

    @Bean
    AuthenticationFacade authenticationFacade() {
        return new AuthenticationFacadeImpl();
    }
}
