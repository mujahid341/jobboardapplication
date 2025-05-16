package jobboardapplication.domain;

public class LoginResponse {

    private String token;

    public LoginResponse() {
        // needed for Jackson deserialization (optional, but good practice)
    }

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
