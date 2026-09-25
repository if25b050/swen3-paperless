package at.fh.technikum.paperless_rest.business.model.label;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

import java.util.UUID;

public record LabelUpdateModel(UUID uuid, String name) implements ValidationModel {
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
