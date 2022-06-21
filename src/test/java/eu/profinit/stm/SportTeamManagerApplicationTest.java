package eu.profinit.stm;

import eu.profinit.stm.configuration.ApplicationConfigurationTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ContextConfiguration(classes = ApplicationConfigurationTest.class)
public class SportTeamManagerApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(SportTeamManagerApplicationTest.class, args);
    }

}
