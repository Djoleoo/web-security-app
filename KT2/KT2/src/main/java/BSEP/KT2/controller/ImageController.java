package BSEP.KT2.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @GetMapping("/logo")
    public ResponseEntity<byte[]> getLogo() throws IOException {
        File file = new File("src/main/resources/static/logo.png");
        InputStream in = new FileInputStream(file);
        byte[] pngBytes = IOUtils.toByteArray(in);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(pngBytes, headers, HttpStatus.OK);
    }
}
