package at.fh.technikum.paperless_rest.document.business.model;

import at.fh.technikum.paperless_rest.common.model.ValidationModel;

public record DocumentCreateModel(String name, byte[] file) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (name == null || name.isBlank()) {
            return "Name is required.";
        }
        if (file == null || file.length == 0) {
            return "File is required.";
        }
        return "";
    }
}
