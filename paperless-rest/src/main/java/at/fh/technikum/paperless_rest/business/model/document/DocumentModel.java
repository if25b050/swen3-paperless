package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;
import at.fh.technikum.paperless_rest.business.model.label.LabelModel;

import java.util.List;

public record DocumentModel(int id, String name, List<LabelModel> labels,
                            String fileUrl) implements ValidationModel {

    @Override
    public String validationLogic() {
        if (fileUrl == null || fileUrl.isBlank()) {
            return "File-URL is required.";
        }
        if (name == null || name.isBlank()) {
            return "Name is required.";
        }
        if (id <= 0) {
            return "ID should be greater than 0.";
        }
        return "";
    }
}
