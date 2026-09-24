package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

import java.util.List;
import java.util.UUID;

public record DocumentUpdateModel(UUID uuid, String name, List<String> labels) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (name == null || name.isBlank()) {
            return "Name is required.";
        }
        if (uuid == null || uuid.toString().isBlank()) {
            return "UUID is required.";
        }
        return "";
    }
}
