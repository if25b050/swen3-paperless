package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

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
