package jobboardapplication.startup.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Define the SecurityFilterChain bean to configure security settings
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // disable CSRF for testing with Postman
                .cors().and()
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/jobboard/auth/**").permitAll()  // ✅ Correctly match your path
                        .anyRequest().authenticated())
                .formLogin(withDefaults());

        return http.build();
    }

    // Define a PasswordEncoder bean for securely encoding passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use BCrypt to hash passwords
    }
}
