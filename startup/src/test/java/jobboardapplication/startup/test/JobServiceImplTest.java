package jobboardapplication.startup.test;

import jakarta.transaction.Transactional;
import jobboardapplication.domain.CreateJobRequest;
import jobboardapplication.domain.JobResponse;
import jobboardapplication.domain.Role;
import jobboardapplication.domain.User;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.api.JobService;
import jobboardapplication.startup.Application;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = Application.class)
@Transactional
public class JobServiceImplTest {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testJobCreation() {
        User employer = new User("Test", "test@x.com", "pass", Role.EMPLOYER);
        employer.setId(UUID.randomUUID().toString());
        userRepository.save(employer);

        Authentication mockAuth = Mockito.mock(Authentication.class);
        Mockito.when(mockAuth.getPrincipal()).thenReturn(employer);

        CreateJobRequest req = new CreateJobRequest();
        req.setTitle("Test Job");
        req.setLocation("Pune");
        req.setDescription("Description");

        JobResponse response = jobService.create(req, mockAuth);

        assertNotNull(response.getId());
    }
}
