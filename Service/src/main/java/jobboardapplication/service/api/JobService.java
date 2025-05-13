package jobboardapplication.service.api;

import jobboardapplication.domain.CreateJobRequest;
import jobboardapplication.domain.JobResponse;
import jobboardapplication.domain.UpdateJobRequest;
import jobboardapplication.domain.User;

import java.util.List;

public interface JobService {
    JobResponse create(CreateJobRequest request, User employer);
    List<JobResponse> listByEmployer(User employer);
    JobResponse update(String jobId, UpdateJobRequest request, User employer);
    void delete(String jobId, User employer);
}
