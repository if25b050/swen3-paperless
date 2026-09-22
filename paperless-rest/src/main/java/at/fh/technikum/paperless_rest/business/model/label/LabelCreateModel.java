package at.fh.technikum.paperless_rest.business.model.label;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

public record LabelCreateModel(String name) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (name == null || name.isBlank()) {
            return "Name is required";
        }
        return "";
    }
}
