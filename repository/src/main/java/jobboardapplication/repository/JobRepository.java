package jobboardapplication.repository;

import jobboardapplication.domain.Job;
import jobboardapplication.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByCreatedBy(User user);

    @Query(value = """
SELECT * FROM jobs j 
WHERE 
  (:title IS NULL OR LOWER(CAST(j.title AS TEXT)) LIKE LOWER(CONCAT('%', :title, '%')))
  AND (:location IS NULL OR LOWER(CAST(j.location AS TEXT)) LIKE LOWER(CONCAT('%', :location, '%')))
  AND (:skills IS NULL OR LOWER(CAST(j.skill AS TEXT)) LIKE LOWER(CONCAT('%', :skills, '%')))
""",
            countQuery = "SELECT COUNT(*) FROM jobs j WHERE " +
                    "(:title IS NULL OR LOWER(CAST(j.title AS TEXT)) LIKE LOWER(CONCAT('%', :title, '%'))) " +
                    "AND (:location IS NULL OR LOWER(CAST(j.location AS TEXT)) LIKE LOWER(CONCAT('%', :location, '%'))) " +
                    "AND (:skills IS NULL OR LOWER(CAST(j.skill AS TEXT)) LIKE LOWER(CONCAT('%', :skills, '%')))",
            nativeQuery = true)
    Page<Job> searchJobs(
            @Param("title") String title,
            @Param("location") String location,
            @Param("skills") String skills,
            Pageable pageable);

    Page<Job> findAll(Specification<Job> spec, Pageable pageable);
}
