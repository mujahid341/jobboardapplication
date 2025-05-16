package jobboardapplication.service.api;

import jobboardapplication.domain.LoginRequest;
import jobboardapplication.domain.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
