package jobboardapplication.repository;

import jobboardapplication.domain.Job;
import jobboardapplication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, String> {
    List<Job> findCreatedByUser(User user);
}
