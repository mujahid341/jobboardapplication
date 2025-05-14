package jobboardapplication.endpoints;

import jobboardapplication.domain.CreateJobRequest;
import jobboardapplication.domain.JobResponse;
import jobboardapplication.domain.User;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.api.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobboard/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createJob(@RequestBody CreateJobRequest request, Authentication auth) {
        try {
            // Fetch the logged-in user from the repository based on the authentication object
            User user = userRepository.findByEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create the job via service
            JobResponse jobResponse = jobService.create(request, user);

            return ResponseEntity.status(HttpStatus.CREATED).body(jobResponse);
        } catch (RuntimeException e) {
            logger.error("Error in job creation: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to create job: " + e.getMessage()));
        }
    }

    // Optional: Structured error response class
    private record ErrorResponse(String message) {}
}
