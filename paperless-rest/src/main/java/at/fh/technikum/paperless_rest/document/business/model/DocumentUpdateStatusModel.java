package at.fh.technikum.paperless_rest.document.business.model;

import at.fh.technikum.paperless_rest.common.model.ValidationModel;

import java.util.UUID;

public record DocumentUpdateStatusModel(UUID uuid, DocumentStatus status) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (uuid == null || uuid.toString().isBlank()) {
            return "UUID is required.";
        }
        if (status == null) {
            return "Status is required.";
        }
        return "";
    }
}