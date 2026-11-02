package at.fh.technikum.paperless_rest.label.business.model;

import at.fh.technikum.paperless_rest.common.model.ValidationModel;

public record LabelCreateModel(String name) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (name == null || name.isBlank()) {
            return "Name is required.";
        }
        return "";
    }
}
