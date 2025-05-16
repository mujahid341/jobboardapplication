package jobboardapplication.service.core;

import jobboardapplication.domain.User;
import jobboardapplication.domain.UserRegisterResponse;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.api.UserService;
import jobboardapplication.domain.RegisterRequest;
import jobboardapplication.service.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserRegisterResponse register(RegisterRequest request) {
        if (request.getName() == null || request.getEmail() == null || request.getPassword() == null || request.getRole() == null) {
            logger.error("Missing required fields: name, email, password");
            throw new ApiException("Name, email, password and role are required", 400);
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.error("Email already registered: {}", request.getEmail());
            throw new ApiException("Email already registered", 409);
        }

        User user = createUser(request);
        User savedUser = userRepository.save(user);

        logger.info("User registered successfully: {}", savedUser);
        return new UserRegisterResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole());
    }

    private User createUser(RegisterRequest request) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        logger.info("Encoded password: {}", user.getPassword());
        user.setRole(request.getRole());
        return user;
    }
}
