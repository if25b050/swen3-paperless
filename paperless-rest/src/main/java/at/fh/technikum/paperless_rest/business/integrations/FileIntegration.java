package at.fh.technikum.paperless_rest.business.integrations;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileIntegration {

    public String saveFile(byte[] file) {
        // TODO actually implement
        return UUID.randomUUID().toString();
    }

    public void deleteFile(String fileUrl) {
        // TODO actually implement
    }
}
