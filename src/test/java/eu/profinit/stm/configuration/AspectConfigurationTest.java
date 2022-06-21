/*
 * AspectConfigurationTest
 *
 * 0.1
 *
 * Author: J. Jansky
 */
package eu.profinit.stm.configuration;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

/**
 * Configuration of aspects
 */
@Configuration
@EnableAspectJAutoProxy
@Profile({"aspects"})
@ComponentScan("eu.profinit.stm.aspect")
public class AspectConfigurationTest {

}
