/*
 * ApplicationConfigurationTest
 *
 * 0.1
 *
 * Author: J. Janský
 */
package cz.profinit.stm.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Basic configuration of an application for tests.
 */
@Configuration
@Import({WebApplicationConfigurationTest.class,
        AspectConfiguration.class})
public class ApplicationConfigurationTest {

}