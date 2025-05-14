package jobboardapplication.startup.config;

import jobboardapplication.service.core.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/jobboard/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/jobboard/job/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/jobboard/job/**").hasRole("EMPLOYER")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())  // ✅ Use lambda-based DSL
                .csrf(AbstractHttpConfigurer::disable)          // ✅ New way to disable CSRF
                .formLogin(AbstractHttpConfigurer::disable);    // ✅ New way to disable form login

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
