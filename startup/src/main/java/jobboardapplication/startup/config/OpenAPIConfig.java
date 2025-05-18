package jobboardapplication.startup.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI jobBoardOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("XcelerateIT Job Board API")
                        .version("1.0.0")
                        .description("API documentation for Job Posting, Searching, and Management."));
    }
}
