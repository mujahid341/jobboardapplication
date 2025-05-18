package jobboardapplication.endpoints;

import jobboardapplication.domain.*;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.api.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
        JobResponse jobResponse = jobService.create(request, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobResponse);
    }

    @GetMapping("/my")
    public ResponseEntity<Object> listMyJobs(Authentication auth) {
        List<JobResponse> jobs = jobService.listByEmployer(auth);
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<Object> updateJob(@PathVariable String jobId, @RequestBody UpdateJobRequest request, Authentication auth) {
        JobResponse jobResponse = jobService.update(jobId, request, auth);
        return ResponseEntity.ok(jobResponse);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Object> deleteJob(@PathVariable String jobId, Authentication auth) {
        JobResponse deletedJob = jobService.delete(jobId, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(deletedJob);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(jobService.search(location, skills, keyword, page, size));
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<Object> getJobById(@PathVariable String jobId) {
        JobResponse job = jobService.getById(jobId);
        return ResponseEntity.ok(job);
    }
}
