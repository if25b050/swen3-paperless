package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

import java.io.OutputStream;
import java.util.UUID;

public record DocumentGetFileModel(UUID uuid, OutputStream fileTarget) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (uuid == null || uuid.toString().isBlank()) {
            return "UUID is required.";
        }
        if (fileTarget == null) {
            return "FileTarget is required.";
        }
        return "";
    }
}
