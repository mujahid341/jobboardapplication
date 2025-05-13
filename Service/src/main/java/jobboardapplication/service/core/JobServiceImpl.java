package jobboardapplication.service.core;

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
    public JobResponse create(CreateJobRequest request, User employer) {

        Job job = new Job();
        job.setId(UUID.randomUUID().toString());
        job.setTitle(request.getTitle());
        job.setLocation(request.getLocation());
        job.setDescription(request.getDescription());
        job.setSkill(request.getSkills());
        job.setCreatedBy(employer);

        jobRepository.save(job);
        logger.error("Job created successfully: {}", job.getTitle());

        return new JobResponse(job);
    }

    @Override
    public List<JobResponse> listByEmployer(User employer) {
        return jobRepository.findCreatedByUser(employer).stream().map(JobResponse::new).toList();
    }

    @Override
    public JobResponse update(String jobId, UpdateJobRequest request, User employer) {

        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCreatedBy().getId().equals(employer.getId())) {
            throw new RuntimeException("You are not authorized to update this job");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setSkill(request.getSkills());
        job.setLocation(request.getLocation());
        job.setCreatedBy(employer);

        jobRepository.save(job);
        logger.error("Job updated successfully: {}", job.getTitle());

        return new JobResponse(job);
    }

    @Override
    public void delete(String jobId, User employer) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCreatedBy().getId().equals(employer.getId())) {
            throw new RuntimeException("You are not authorized to delete this job");
        }

        jobRepository.delete(job);
    }
}
