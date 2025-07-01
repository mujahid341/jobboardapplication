package jobboardapplication.service.api;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ResumeMatcherService {
    String sendResumeToPythonAPI(MultipartFile resumeFile) throws IOException;
}
