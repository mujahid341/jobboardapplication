package jobboardapplication.repository;

import jobboardapplication.domain.Job;
import jobboardapplication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, String> {
    List<Job> findByCreatedBy(User user);

    @Query("SELECT j FROM Job j WHERE " +
            "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:skills IS NULL OR LOWER(j.skill) LIKE LOWER(CONCAT('%', :skills, '%'))) AND " +
            "(:keyword IS NULL OR (LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    List<Job> searchJobs(@Param("location") String location,
                         @Param("skills") String skills,
                         @Param("keyword") String keyword);
}
