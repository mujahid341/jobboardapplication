package jobboardapplication.service.core;

import jakarta.transaction.Transactional;
import jobboardapplication.domain.*;
import jobboardapplication.repository.JobRepository;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.api.JobService;
import jobboardapplication.service.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Override
    @Transactional
    public JobResponse create(CreateJobRequest request, Authentication auth) {

        if (request.getTitle() == null || request.getLocation() == null || request.getDescription() == null) {
            throw new RuntimeException("Title, location, and description are required");
        }

        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Job job = new Job();
        job.setId(UUID.randomUUID().toString());
        job.setTitle(request.getTitle());
        job.setLocation(request.getLocation());
        job.setDescription(request.getDescription());
        job.setSkill(request.getSkills());
        job.setCreatedBy(user);

        try {

            jobRepository.save(job);
            logger.info("Job saved successfully: {}", job);
        } catch (Exception e) {
            logger.error("Error creating job: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving the job");
        }

        return new JobResponse(job);
    }

    @Override
    public List<JobResponse> listByEmployer(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return jobRepository.findByCreatedBy(user).stream().map(JobResponse::new).toList();
    }

    @Override
    public JobResponse update(String jobId, UpdateJobRequest request, Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setSkill(request.getSkills());
        job.setLocation(request.getLocation());
        job.setCreatedBy(user);

        jobRepository.save(job);
        logger.info("Job updated successfully: {}", job);

        return new JobResponse(job);
    }

    @Override
    public JobResponse delete(String jobId, Authentication auth) {

        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

       JobResponse deletedJob = new JobResponse(job);
        jobRepository.delete(job);
        logger.info("Job deleted successfully: {}", job);
        return deletedJob;
    }
}
