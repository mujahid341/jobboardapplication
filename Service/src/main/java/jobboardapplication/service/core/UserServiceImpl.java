package jobboardapplication.service.core;

import jobboardapplication.domain.User;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.api.UserService;
import jobboardapplication.domain.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @Transactional // Adding @Transactional here ensures that the method runs within a transaction
    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.error("Email already registered: {}", request.getEmail());
            throw new RuntimeException("new Email already registered");
        }

        User user = createUser(request);

        userRepository.save(user); // This operation will now be part of the transaction
        logger.debug("User registered successfully: {}", user.getEmail());
    }

    private static User createUser(RegisterRequest request) {
        User user = new User();
        // Do not set the ID manually, let JPA handle it.
        user.setId(UUID.randomUUID().toString());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        return user;
    }
}
