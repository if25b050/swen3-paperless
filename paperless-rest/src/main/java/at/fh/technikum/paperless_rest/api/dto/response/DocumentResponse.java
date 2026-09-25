package at.fh.technikum.paperless_rest.api.dto.response;

import at.fh.technikum.paperless_rest.business.model.label.LabelModel;

import java.util.List;

public record DocumentResponse(String uuid, String name, List<LabelModel> labels, String fileUrl) {
}
