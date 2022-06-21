package eu.profinit.stm;

import eu.profinit.stm.configuration.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;


@SpringBootApplication(scanBasePackages = "eu.profinit.stm", exclude = {
        //DataSourceAutoConfiguration.class,
        //DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class})
@ContextConfiguration(classes = ApplicationConfiguration.class)
@ActiveProfiles("Main")
public class SportTeamManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportTeamManagerApplication.class, args);
    }

}
