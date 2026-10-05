package at.fh.technikum.paperless_rest.business.integration;

import java.io.OutputStream;
import java.util.UUID;

public interface FileIntegration {
    void getFile(UUID documentId, OutputStream fileTarget);

    void saveFile(UUID documentId, byte[] file);

    void deleteFile(UUID documentId);
}
