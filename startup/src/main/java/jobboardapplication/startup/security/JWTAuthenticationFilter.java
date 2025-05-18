package jobboardapplication.startup.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jobboardapplication.common.security.JWTUtil;
import jobboardapplication.domain.User;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public JWTAuthenticationFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtUtil.isValid(token)) {
                String email = jwtUtil.extractEmail(token);
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new ApiException("User not found for email: " + email, HttpStatus.NOT_FOUND.value()));

                if (user != null) {
                    // ✅ Inject role properly as ROLE_EMPLOYER
                    var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(auth);

                    // ✅ Debug logs for clarity
                    System.out.println("✅ Authenticated Email: " + email);
                    System.out.println("✅ Authorities: " + authorities);
                    System.out.println("✅ URI: " + request.getRequestURI());
                    System.out.println("✅ HTTP Method: " + request.getMethod());
                }
            }
        }

        chain.doFilter(request, response);
    }
}
