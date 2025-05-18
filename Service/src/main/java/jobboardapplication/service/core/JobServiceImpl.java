package jobboardapplication.service.core;

import jakarta.transaction.Transactional;
import jobboardapplication.domain.*;
import jobboardapplication.repository.JobRepository;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.api.JobService;
import jobboardapplication.service.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    @Override
    @Transactional
    public JobResponse create(CreateJobRequest request, Authentication auth) {
        if (request.getTitle() == null || request.getLocation() == null || request.getDescription() == null) {
            throw new ApiException("Title, location, and description are required", HttpStatus.BAD_REQUEST.value());
        }

        User user = (User) auth.getPrincipal();

        Job job = new Job();
        job.setId(UUID.randomUUID().toString());
        job.setTitle(request.getTitle());
        job.setLocation(request.getLocation());
        job.setDescription(request.getDescription());
        job.setSkill(request.getSkills());
        job.setCreatedBy(user);

        jobRepository.save(job);
        logger.info("Job saved successfully: {}", job);

        return new JobResponse(job);
    }

    @Override
    public List<JobResponse> listByEmployer(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return jobRepository.findByCreatedBy(user)
                .stream()
                .map(JobResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public JobResponse update(String jobId, UpdateJobRequest request, Authentication auth) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException("Job not found", HttpStatus.NOT_FOUND.value()));

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setSkill(request.getSkills());
        job.setLocation(request.getLocation());

        jobRepository.save(job);
        logger.info("Job updated successfully: {}", job);

        return new JobResponse(job);
    }

    @Override
    @Transactional
    public JobResponse delete(String jobId, Authentication auth) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException("Job not found", HttpStatus.NOT_FOUND.value()));

        User currentUser = (User) auth.getPrincipal();

        if (!job.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ApiException("You are not authorized to delete this job", HttpStatus.FORBIDDEN.value());
        }

        JobResponse deletedJob = new JobResponse(job);
        jobRepository.delete(job);
        logger.info("Job deleted successfully: {}", deletedJob);

        return deletedJob;
    }


    @Override
    public Page<JobResponse> search(String location, String skills, String keyword, int page, int size) {
        return jobRepository
                .searchJobs(location, skills, keyword, PageRequest.of(page, size)).map(JobResponse::new);
    }

    @Override
    public JobResponse getById(String jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ApiException("Job not found", HttpStatus.NOT_FOUND.value()));
        return new JobResponse(job);
    }
}
