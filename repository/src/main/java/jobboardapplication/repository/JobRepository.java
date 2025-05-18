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

   /* @Query("SELECT j FROM Job j WHERE " +
            "(:location IS NULL OR LOWER(CAST(COALESCE(j.location, '') AS string)) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:skills IS NULL OR LOWER(CAST(COALESCE(j.skill, '') AS string)) LIKE LOWER(CONCAT('%', :skills, '%'))) AND " +
            "(:keyword IS NULL OR (LOWER(CAST(COALESCE(j.title, '') AS string)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(CAST(COALESCE(j.description, '') AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    Page<Job> searchJobs(@Param("location") String location,
                         @Param("skills") String skills,
                         @Param("keyword") String keyword,
                         Pageable pageable);*/

    @Query(value = """
    SELECT * FROM jobs j 
    WHERE 
      (:location IS NULL OR LOWER(CAST(j.location AS TEXT)) LIKE LOWER(CONCAT('%', :location, '%')))
      AND (:skills IS NULL OR LOWER(CAST(j.skill AS TEXT)) LIKE LOWER(CONCAT('%', :skills, '%')))
      AND (:keyword IS NULL OR (
        LOWER(CAST(j.title AS TEXT)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(CAST(j.description AS TEXT)) LIKE LOWER(CONCAT('%', :keyword, '%'))
      ))
    """,
            countQuery = "SELECT COUNT(*) FROM jobs j WHERE " +
                    "(:location IS NULL OR LOWER(CAST(j.location AS TEXT)) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
                    "(:skills IS NULL OR LOWER(CAST(j.skill AS TEXT)) LIKE LOWER(CONCAT('%', :skills, '%'))) AND " +
                    "(:keyword IS NULL OR (LOWER(CAST(j.title AS TEXT)) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                    "LOWER(CAST(j.description AS TEXT)) LIKE LOWER(CONCAT('%', :keyword, '%'))))",
            nativeQuery = true)
    Page<Job> searchJobs(@Param("location") String location,
                         @Param("skills") String skills,
                         @Param("keyword") String keyword,
                         Pageable pageable);

    Page<Job> findAll(Specification<Job> spec, Pageable pageable);
}
