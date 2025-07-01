package jobboardapplication.service.core;

import jobboardapplication.service.api.ResumeMatcherService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
public class ResumeMatcherServiceImpl implements ResumeMatcherService {
    @Override
    public String sendResumeToPythonAPI(MultipartFile resumeFile) throws IOException {
        String pythonApiUrl = "http://localhost:5000/match-resume";
        RestTemplate restTemplate = new RestTemplate();

        // Wrap file as ByteArrayResource
        ByteArrayResource resource = new ByteArrayResource(resumeFile.getBytes()) {
            @Override
            public String getFilename() {
                return resumeFile.getOriginalFilename(); // Required for Flask to read filename
            }
        };

        // Build multipart body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("resume_file", resource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Combine the file + headers into one object (HttpEntity) to send to the server.
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send POST request to the Python API
        ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

        return response.getBody();
    }
}
