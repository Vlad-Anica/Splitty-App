package server.services.implementations;


import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import server.services.interfaces.DownloadService;

import java.io.*;

@Service
public class DownloadServiceImpl implements DownloadService {


    @Override
    public ResponseEntity<InputStreamResource>  download(String path) throws IOException {

        Reader reader = new FileReader(path);
        String s = "";
        int i;
        while ((i = reader.read()) != -1) {

            s += (char)i;
        }
        byte[] bytes = s.getBytes();

        InputStream input = new ByteArrayInputStream(bytes);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=template.txt")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(bytes.length)
                .body(new InputStreamResource(input));
    }
}
