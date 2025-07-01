package jobboardapplication.endpoints;

import jobboardapplication.service.api.ResumeMatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeMatcherService resumeMatcherService;

    @PostMapping("/match")
    public String matchResume(@RequestParam("file") MultipartFile resumeFile) throws Exception {
        return resumeMatcherService.sendResumeToPythonAPI(resumeFile);
    }
}
