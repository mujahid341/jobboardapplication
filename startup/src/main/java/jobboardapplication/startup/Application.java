package jobboardapplication.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "jobboardapplication")
@EnableJpaRepositories(basePackages = "jobboardapplication.repository")
@EntityScan(basePackages = "jobboardapplication.domain")

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
