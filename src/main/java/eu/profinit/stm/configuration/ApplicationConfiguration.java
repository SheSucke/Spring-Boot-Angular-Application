/*
 * ApplicationConfiguration
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Basic configuration of an application.
 */
@Configuration
@Import({//WebApplicationConfiguration.class,
        WebSecurityConfig.class,
        WebConfig.class,
        //AuthenticationConfiguration.class,
        //AuthorizationConfiguration.class,
        //AspectConfiguration.class,
        MyBatisConfiguration.class})
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class ApplicationConfiguration {


}
