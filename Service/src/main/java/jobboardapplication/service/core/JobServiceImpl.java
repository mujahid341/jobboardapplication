package jobboardapplication.service.core;

import jakarta.transaction.Transactional;
import jobboardapplication.domain.*;
import jobboardapplication.repository.JobRepository;
import jobboardapplication.service.api.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Override
    @Transactional
    public JobResponse create(CreateJobRequest request, User employer) {
        if (employer == null) {
            throw new RuntimeException("Employer not found");
        }

        if (request.getTitle() == null || request.getLocation() == null || request.getDescription() == null) {
            throw new RuntimeException("Title, location, and description are required");
        }

        // Creating a new Job instance
        Job job = new Job();
        job.setId(UUID.randomUUID().toString());
        job.setTitle(request.getTitle());
        job.setLocation(request.getLocation());
        job.setDescription(request.getDescription());
        job.setSkill(request.getSkills());
        job.setCreatedBy(employer);

        try {
            // Logging the job before saving
            logger.info("Attempting to save job: {}", job);
            jobRepository.save(job);
            logger.info("Job created successfully: {}", job.getTitle());
        } catch (Exception e) {
            logger.error("Error creating job: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving the job");
        }

        return new JobResponse(job);
    }

    @Override
    public List<JobResponse> listByEmployer(User employer) {
        return jobRepository.findByCreatedBy(employer).stream().map(JobResponse::new).toList();
    }

    @Override
    public JobResponse update(String jobId, UpdateJobRequest request, User employer) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCreatedBy().getId().equals(employer.getId())) {
            throw new RuntimeException("Not authorized to update this job");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setSkill(request.getSkills());
        job.setLocation(request.getLocation());
        job.setCreatedBy(employer);

        jobRepository.save(job);
        logger.info("Job updated successfully: {}", job.getTitle());

        return new JobResponse(job);
    }

    @Override
    public void delete(String jobId, User employer) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCreatedBy().getId().equals(employer.getId())) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        jobRepository.delete(job);
        logger.info("Job deleted successfully: {}", job.getTitle());
    }
}
