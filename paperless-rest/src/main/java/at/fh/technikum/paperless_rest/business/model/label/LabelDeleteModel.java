package at.fh.technikum.paperless_rest.business.model.label;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

public record LabelDeleteModel(int id) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (id <= 0) {
            return "ID should be greater than 0.";
        }
        return "";
    }
}
