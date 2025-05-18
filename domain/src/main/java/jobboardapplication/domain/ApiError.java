package jobboardapplication.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API error response")
public class ApiError {
    @Schema(description = "Error message", example = "Unexpected error occurred")
    private final String message;

    public ApiError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
