package gr.uoa.di.citizen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// Λέμε στο Spring Boot πού να βρει την κλάση Citizen (στο άλλο module)
@EntityScan(basePackages = "gr.uoa.di.citizen.domain")
// Λέμε στο Spring Boot πού να βρει το Repository
@EnableJpaRepositories(basePackages = "gr.uoa.di.citizen.repository")
public class CitizenApplication {

    public static void main(String[] args) {
        SpringApplication.run(CitizenApplication.class, args);
    }
}