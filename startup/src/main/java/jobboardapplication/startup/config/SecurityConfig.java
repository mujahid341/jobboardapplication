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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ✅ Allow Swagger / OpenAPI access without auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()

                        // ✅ Allow public POST requests for auth (login, register)
                        .requestMatchers(HttpMethod.POST, "/api/jobboard/auth/**").permitAll()

                        // ✅ Allow public GET requests for job search
                        .requestMatchers(HttpMethod.GET, "/api/jobboard/job/**").permitAll()

                        // 🔐 Protect employer-only endpoints
                        .requestMatchers(HttpMethod.GET, "/api/jobboard/job/my").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.POST, "/api/jobboard/job/**").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.PUT, "/api/jobboard/job/**").hasRole("EMPLOYER")
                        .requestMatchers(HttpMethod.DELETE, "/api/jobboard/job/**").hasRole("EMPLOYER")

                        // 🔐 Any other endpoint requires authentication
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
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
