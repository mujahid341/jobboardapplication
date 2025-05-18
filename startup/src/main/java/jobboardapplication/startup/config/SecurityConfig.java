package jobboardapplication.startup.config;

import jobboardapplication.common.security.JWTUtil;
import jobboardapplication.service.core.CustomUserDetailService;
import jobboardapplication.startup.security.JWTAuthenticationFilter;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.startup.security.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService customUserDetailsService;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        return new JWTAuthenticationFilter(jwtUtil, userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JWTAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // 🔓 Swagger/OpenAPI access for developers
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // 👤 Public Auth API
                        .requestMatchers(HttpMethod.POST, "/jobboard/auth/**").permitAll()

                        // 📄 Public Job Search API
                        .requestMatchers(HttpMethod.GET, "/jobboard/job/**").permitAll()

                        // 🧑‍💼 Employer-only APIs
                        .requestMatchers(HttpMethod.GET, "/jobboard/job/my").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.POST, "/jobboard/job/**").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.PUT, "/jobboard/job/**").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.DELETE, "/jobboard/job/**").hasRole("EMPLOYER")

                        // 🔐 All others require authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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
