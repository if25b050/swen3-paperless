package at.fh.technikum.paperless_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"at.fh.technikum.paperless_rest", "at.fh.technikum.paperless_common"})
public class PaperlessRestApplication {

    static void main(String[] args) {
        SpringApplication.run(PaperlessRestApplication.class, args);
    }

}
