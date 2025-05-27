package jobboardapplication.startup.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import jobboardapplication.domain.LoginRequest;
import jobboardapplication.domain.LoginResponse;
import jobboardapplication.service.api.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest req = new LoginRequest("user@example.com", "pass");

        Mockito.when(authService.login(any()))
                .thenReturn(new LoginResponse("mock-token"));

        mockMvc.perform(post("/api/jobboard/auth/login") // ✅ updated path
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-token"));
    }
}
