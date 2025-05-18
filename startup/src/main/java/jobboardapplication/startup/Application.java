package jobboardapplication.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "jobboardapplication")
@EnableJpaRepositories(basePackages = "jobboardapplication.repository")
@EntityScan(basePackages = "jobboardapplication.domain")
@EnableConfigurationProperties
@EnableJpaAuditing // Required for @CreatedDate and @LastModifiedDate to work
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
