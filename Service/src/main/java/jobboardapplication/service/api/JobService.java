package jobboardapplication.service.api;

import jobboardapplication.domain.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface JobService {
    JobResponse create(CreateJobRequest request, Authentication auth);
    List<JobResponse> listByEmployer(Authentication auth);
    JobResponse update(String jobId, UpdateJobRequest request, Authentication auth);
    JobResponse delete(String jobId, Authentication auth);
}
