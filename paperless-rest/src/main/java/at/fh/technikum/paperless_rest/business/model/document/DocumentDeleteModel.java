package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

public record DocumentDeleteModel(int id) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (id <= 0) {
            return "ID should be greater than 0";
        }
        return "";
    }
}
