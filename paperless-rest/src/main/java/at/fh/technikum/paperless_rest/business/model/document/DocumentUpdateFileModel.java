package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

public record DocumentUpdateFileModel(int id, byte[] file) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (id <= 0) {
            return "ID should be greater than 0";
        }
        if (file == null || file.length == 0) {
            return "File is required";
        }
        return "";
    }
}
