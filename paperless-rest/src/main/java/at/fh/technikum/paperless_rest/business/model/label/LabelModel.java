package at.fh.technikum.paperless_rest.business.model.label;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

public record LabelModel(int id, String name) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (name == null || name.isBlank()) {
            return "Name is required";
        }
        if (id <= 0) {
            return "ID should be greater than 0";
        }
        return "";
    }
}
