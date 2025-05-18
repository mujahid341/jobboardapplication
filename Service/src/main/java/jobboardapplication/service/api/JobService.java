package jobboardapplication.service.api;

import jobboardapplication.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface JobService {
    JobResponse create(CreateJobRequest request, Authentication auth);
    List<JobResponse> listByEmployer(Authentication auth);
    JobResponse update(String jobId, UpdateJobRequest request, Authentication auth);
    JobResponse delete(String jobId, Authentication auth);
   // List<JobResponse> search(String location, String skills, String keyword);
    JobResponse getById(String jobId);
    Page<JobResponse> search(String location, String skills, String keyword, int page, int size);

}
