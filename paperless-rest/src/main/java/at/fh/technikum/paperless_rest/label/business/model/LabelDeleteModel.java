package at.fh.technikum.paperless_rest.label.business.model;

import at.fh.technikum.paperless_rest.common.model.ValidationModel;

import java.util.UUID;

public record LabelDeleteModel(UUID uuid) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (uuid == null || uuid.toString().isBlank()) {
            return "UUID is required.";
        }
        return "";
    }
}
