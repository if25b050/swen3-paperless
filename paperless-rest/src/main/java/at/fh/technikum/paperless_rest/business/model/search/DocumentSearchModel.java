package at.fh.technikum.paperless_rest.business.model.search;

import at.fh.technikum.paperless_rest.business.model.ValidationModel;

public record DocumentSearchModel(String search, int page, int pageSize) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (search == null || search.isBlank()) {
            return "Search is required.";
        }
        if (page <= 0) {
            return "Page must at least be 1.";
        }
        if (pageSize <= 0) {
            return "PageSize must at least be 1.";
        }
        return "";
    }
}
