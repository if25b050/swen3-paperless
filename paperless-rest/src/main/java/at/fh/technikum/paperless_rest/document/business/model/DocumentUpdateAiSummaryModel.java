package at.fh.technikum.paperless_rest.document.business.model;

import at.fh.technikum.paperless_rest.common.model.ValidationModel;

import java.util.UUID;

public record DocumentUpdateAiSummaryModel(UUID uuid, String aiSummary) implements ValidationModel {
    @Override
    public String validationLogic() {
        if (uuid == null || uuid.toString().isBlank()) {
            return "UUID is required.";
        }
        if (aiSummary == null || aiSummary.isBlank()) {
            return "AI-Summary is required.";
        }
        return "";
    }
}