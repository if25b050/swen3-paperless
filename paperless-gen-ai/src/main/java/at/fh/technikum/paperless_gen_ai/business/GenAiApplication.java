package at.fh.technikum.paperless_gen_ai.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"at.fh.technikum.paperless_common", "at.fh.technikum.paperless_gen_ai"})
public class GenAiApplication {
    static void main(String[] args) {
        SpringApplication.run(GenAiApplication.class, args);
    }
}
