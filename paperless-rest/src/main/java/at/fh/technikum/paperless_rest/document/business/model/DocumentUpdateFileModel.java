package at.fh.technikum.paperless_rest.document.business.model;

import at.fh.technikum.paperless_rest.common.model.ValidationModel;

import java.util.UUID;

public record DocumentUpdateFileModel(UUID uuid, byte[] file) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (uuid == null || uuid.toString().isBlank()) {
            return "UUID is required.";
        }
        if (file == null || file.length == 0) {
            return "File is required.";
        }
        return "";
    }
}
