package jobboardapplication.service.api;

import jobboardapplication.domain.RegisterRequest;
import jobboardapplication.domain.UserRegisterResponse;

public interface UserService {
    UserRegisterResponse register(RegisterRequest request);
}
