package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

import java.util.UUID;

public record DocumentDeleteModel(UUID uuid) implements ValidationModel {
    
    @Override
    public String validationLogic() {
        if (uuid == null || uuid.toString().isBlank()) {
            return "UUID is required.";
        }
        return "";
    }
}
