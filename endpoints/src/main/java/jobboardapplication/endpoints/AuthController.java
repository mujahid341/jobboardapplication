package jobboardapplication.endpoints;

import jobboardapplication.service.api.UserService;
import jobboardapplication.domain.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobboard/auth")
public class AuthController {
   private Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
   private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Validate the request
            if (request.getName() == null || request.getEmail() == null || request.getPassword() == null) {
                return ResponseEntity.badRequest().body("Name, email, and password are required");
            }
            if (request.getRole() == null) {
                return ResponseEntity.badRequest().body("Role is required");
            }
            userService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (Exception e) {
            logger.error("Error during registration: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
}
