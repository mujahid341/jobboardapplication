package jobboardapplication.repository;

import jobboardapplication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> { // Change String to Long
    Optional<User> findByEmail(String email);
}
