package jobboardapplication.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "jobboardapplication.startup",
        "jobboardapplication.repository",
        "jobboardapplication.domain",
        "jobboardapplication.service",
        "jobboardapplication.common",
        "jobboardapplication.endpoints"  // ✅ ensures Swagger sees your controllers
})
@EnableJpaRepositories(basePackages = "jobboardapplication.repository")
@EntityScan(basePackages = "jobboardapplication.domain")
@EnableConfigurationProperties
@EnableJpaAuditing
public class Application {
    public static void main(String[] args) {
        System.out.println("ControllerAdviceBean loaded from: " +
                org.springframework.web.method.ControllerAdviceBean.class
                        .getProtectionDomain().getCodeSource().getLocation());

        SpringApplication.run(Application.class, args);
    }
}
