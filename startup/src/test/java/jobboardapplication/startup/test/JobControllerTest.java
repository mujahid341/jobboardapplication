package jobboardapplication.startup.test;

import jobboardapplication.domain.CreateJobRequest;
import jobboardapplication.domain.Role;
import jobboardapplication.domain.User;
import jobboardapplication.repository.JobRepository;
import jobboardapplication.repository.UserRepository;
import jobboardapplication.service.api.JobService;
import jobboardapplication.service.api.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobService jobService;

    @BeforeEach
    void cleanUp() {
        jobRepository.deleteAll(); // ✅ Clear previous jobs before each test
    }
    @Test
    public void testJobSearchPagination() {
        User employer = new User("Paginated", "paginated@x.com", "pass", Role.EMPLOYER);
        employer.setId(UUID.randomUUID().toString());
        userRepository.save(employer);

        Authentication mockAuth = Mockito.mock(Authentication.class);
        Mockito.when(mockAuth.getPrincipal()).thenReturn(employer);

        // Insert 15 dummy jobs
        for (int i = 1; i <= 15; i++) {
            CreateJobRequest req = new CreateJobRequest();
            req.setTitle("Test Job " + i);
            req.setLocation("Pune");
            req.setDescription("Description " + i);
            jobService.create(req, mockAuth);
        }

        // Search with pagination
        var resultPage = jobService.search("Pune", null, null, 0, 5);  // Page 0, size 5

        assertNotNull(resultPage);
        assertEquals(5, resultPage.getContent().size());
        assertEquals(15, resultPage.getTotalElements());
        assertEquals(3, resultPage.getTotalPages());
    }
}
