/*
 * StubRepositoryConfiguration
 *
 * 0.1
 *
 * Author: J. Janský
 */
package eu.profinit.stm.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * Configuration bringing STUB services.
 */
@Configuration
@Profile({"stub_service"})
@ComponentScan(basePackages = "eu.profinit.stm.repository")
@Import(PasswordEncoderConfiguration.class)
public class StubServiceConfiguration {
}
