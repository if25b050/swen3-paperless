package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

import java.util.List;

public record DocumentUpdateModel(int id, String name, List<String> labels) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (name == null || name.isBlank()) {
            return "Name is required.";
        }
        if (id <= 0) {
            return "ID should be greater than 0.";
        }
        return "";
    }
}
