package jobboardapplication.endpoints;

import jobboardapplication.domain.*;
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

import java.util.List;

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
            JobResponse jobResponse = jobService.create(request, auth);

            return ResponseEntity.status(HttpStatus.CREATED).body(jobResponse);
        } catch (RuntimeException e) {
            logger.error("Error in job creation: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to create job: " + e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<Object> listMyJobs(Authentication auth) {
        try {
            List<JobResponse> jobs = jobService.listByEmployer(auth);

            logger.error(jobs.toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(jobs);
        } catch (Exception e) {
            logger.error("Error in fetching jobs: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to fetch jobs: " + e.getMessage()));
        }
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<?> updateJob(@PathVariable String jobId, @RequestBody UpdateJobRequest request, Authentication auth) {

        try {

            JobResponse jobResponse = jobService.update(jobId, request, auth);
            logger.error("Job updated successfully: {}", jobResponse);
            return ResponseEntity.ok(jobResponse);

        } catch (RuntimeException e) {
            logger.error("Error in job update: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to update job: " + e.getMessage()));
        }
    }
    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> deleteJob(@PathVariable String jobId, Authentication auth) {

        try {
           JobResponse deletedJob = jobService.delete(jobId, auth);
            return ResponseEntity.ok(deletedJob);
        } catch (RuntimeException e) {
            logger.error("Error in job deletion: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to delete job: " + e.getMessage()));
        }
    }
    // Optional: Structured error response class
    private record ErrorResponse(String message) {}
}
