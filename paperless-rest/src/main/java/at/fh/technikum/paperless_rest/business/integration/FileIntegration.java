package at.fh.technikum.paperless_rest.business.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static at.fh.technikum.paperless_rest.business.BusinessUtil.kv;

@Service
@Slf4j
public class FileIntegration {

    public String saveFile(byte[] file) {
        // TODO actually implement
        log.debug("A file will be saved");
        String path = UUID.randomUUID().toString();
        log.info("A file was saved under {}", kv("path", path));
        return path;
    }

    public void deleteFile(String fileUrl) {
        // TODO actually implement
        log.debug("The file behind {} will be deleted", fileUrl);
        log.info("The file behind {} was deleted", fileUrl);

    }
}
