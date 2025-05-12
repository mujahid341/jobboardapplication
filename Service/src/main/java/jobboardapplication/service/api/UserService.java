package jobboardapplication.service.api;

import jobboardapplication.domain.RegisterRequest;

public interface UserService {
    void register(RegisterRequest request);
}
