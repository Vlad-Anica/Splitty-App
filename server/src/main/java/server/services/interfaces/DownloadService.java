package server.services.interfaces;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface DownloadService {

    public ResponseEntity<InputStreamResource> download(String path) throws IOException;
}
