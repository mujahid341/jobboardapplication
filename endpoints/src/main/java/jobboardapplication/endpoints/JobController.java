package jobboardapplication.endpoints;

import io.swagger.v3.oas.annotations.tags.Tag;
import jobboardapplication.domain.JobResponse;
import jobboardapplication.domain.CreateJobRequest;
import jobboardapplication.domain.UpdateJobRequest;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.api.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Job Management", description = "Operations related to job posting and searching")
@CrossOrigin(origins = "http://localhost:5173") // ✅ CORS for frontend
@RestController
@RequestMapping("/api/jobboard/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createJob(@RequestBody CreateJobRequest request, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.create(request, auth));
    }

    @GetMapping("/my")
    public ResponseEntity<Object> listMyJobs(Authentication auth) {
        return ResponseEntity.ok(jobService.listByEmployer(auth));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<Object> updateJob(@PathVariable String jobId, @RequestBody UpdateJobRequest request, Authentication auth) {
        return ResponseEntity.ok(jobService.update(jobId, request, auth));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Object> deleteJob(@PathVariable String jobId, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.delete(jobId, auth));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skills,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.search(location, skills, title, page, size));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Object> getJobById(@PathVariable String jobId) {
        return ResponseEntity.ok(jobService.getById(jobId));
    }

    // ✅ New endpoint for resume matcher integration
    @GetMapping("/all/public")
    public ResponseEntity<List<JobResponse>> getAllJobsForMatcher() {
        return ResponseEntity.ok(jobService.getAllJobsForMatcher());
    }
}
