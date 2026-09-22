package at.fh.technikum.paperless_rest.presentation.dto.response;

import at.fh.technikum.paperless_rest.business.model.LabelModel;

import java.util.List;

public record DocumentResponse(int id, String name, List<LabelModel> labels, String fileUrl) {
}
