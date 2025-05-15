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
    public ResponseEntity<Object> createJob(@RequestBody CreateJobRequest request, Authentication auth) {
        try {
            JobResponse jobResponse = jobService.create(request, auth);
            return ResponseEntity.status(HttpStatus.CREATED).body(jobResponse);
        } catch (RuntimeException e) {
            logger.error("Error in job creation: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to create job: " + e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<Object> listMyJobs(Authentication auth) {
        try {
            List<JobResponse> jobs = jobService.listByEmployer(auth);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            logger.error("Error in fetching jobs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to fetch jobs: " + e.getMessage()));
        }
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<Object> updateJob(@PathVariable String jobId, @RequestBody UpdateJobRequest request, Authentication auth) {
        try {
            JobResponse jobResponse = jobService.update(jobId, request, auth);
            logger.info("Job updated successfully: {}", jobResponse);
            return ResponseEntity.ok(jobResponse);
        } catch (RuntimeException e) {
            logger.error("Error in job update: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to update job: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Object> deleteJob(@PathVariable String jobId, Authentication auth) {
        try {
            JobResponse deletedJob = jobService.delete(jobId, auth);
            return ResponseEntity.status(HttpStatus.CREATED).body(deletedJob);
        } catch (RuntimeException e) {
            logger.error("Error in job deletion: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to delete job: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchJobs(@RequestParam(required = false) String location,
                                             @RequestParam(required = false) String skills,
                                             @RequestParam(required = false) String keyword) {
        try {
            List<JobResponse> jobs = jobService.search(location, skills, keyword);
            return ResponseEntity.ok(jobs);
        } catch (Exception e) {
            logger.error("Error in searching jobs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Failed to search jobs: " + e.getMessage()));
        }
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Object> getJobById(@PathVariable String jobId) {
        try {
            JobResponse job = jobService.getById(jobId);
            return ResponseEntity.ok(job);
        } catch (RuntimeException e) {
            logger.error("Error in fetching job by ID: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Failed to fetch job: " + e.getMessage()));
        }
    }

    private record ErrorResponse(String message) {}
}
