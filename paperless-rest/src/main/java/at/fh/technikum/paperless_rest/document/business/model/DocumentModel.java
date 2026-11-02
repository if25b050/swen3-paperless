package at.fh.technikum.paperless_rest.document.business.model;

import at.fh.technikum.paperless_rest.common.model.ValidationModel;
import at.fh.technikum.paperless_rest.label.business.model.LabelModel;

import java.util.List;
import java.util.UUID;

public record DocumentModel(UUID uuid, String name, List<LabelModel> labels) implements ValidationModel {

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
