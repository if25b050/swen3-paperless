package at.fh.technikum.paperless_ocr.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"at.fh.technikum.paperless_common", "at.fh.technikum.paperless_ocr"})
public class OcrApplication {
    static void main(String[] args) {
        SpringApplication.run(OcrApplication.class, args);
    }
}
