package jobboardapplication.service.core;

import jobboardapplication.domain.User;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.api.UserService;
import jobboardapplication.domain.RegisterRequest;
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
    @Transactional // Adding @Transactional here ensures that the method runs within a transaction
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.error("Email already registered: {}", request.getEmail());
            throw new RuntimeException("Email already registered");
        }

        User user = createUser(request);

        userRepository.save(user); // This operation will now be part of the transaction
        logger.error("User registered successfully: {}", user.getEmail());
    }

    private User createUser(RegisterRequest request) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        logger.error("Encoded password: {}", user.getPassword());
        user.setRole(request.getRole());
        return user;
    }
}
