package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.services.interfaces.DownloadService;

import java.io.IOException;

@RestController
@RequestMapping("/api/downloads")
public class DownloadController {

    private DownloadService service;
    @Autowired
    public DownloadController(DownloadService service) {
         this.service = service;
    }
    @GetMapping("/languageTemplate")
    public ResponseEntity<InputStreamResource>  downloadTemplate() throws IOException {
        return service.download("client/src/main/resources/languages/language_template.txt");
    }

}
