package at.fh.technikum.paperless_rest.business.model.document;

import at.fh.technikum.paperless_rest.business.model.label.LabelModel;

import java.util.List;

public record DocumentModel(int id, String name, List<LabelModel> labels, String fileUrl) {
}
