/*
 * WebApplicationConfiguration
 *
 * 0.1
 *
 * Author: J. Janský
 */
package cz.profinit.sportTeamManager.configuration;

import cz.profinit.sportTeamManager.repositories.UserRepository;
import cz.profinit.sportTeamManager.stubs.stubRepositories.StubUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Configuration of a web services, mainly of authorization provider and http security protocols for testing.
 */
@Configuration
@Profile({"webTest"})
@EnableWebMvc
public class WebApplicationConfigurationUnitTests extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private DaoAuthenticationProvider authenticationProvider;
    private UserRepository userRepository = new StubUserRepository();
    /**
     * Adds stubs authenticated users to in memory storage.
     */
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password(passwordEncoder.encode("1")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder.encode("user2Pass")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("a")).roles("ADMIN");
    }


    /**
     * Sets http security authorization protocols.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/**", "/home", "/user/registration", "/login").permitAll();
    }




}

