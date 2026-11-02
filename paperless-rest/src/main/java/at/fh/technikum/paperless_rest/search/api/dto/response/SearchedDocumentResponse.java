package at.fh.technikum.paperless_rest.search.api.dto.response;

import at.fh.technikum.paperless_rest.label.business.model.LabelModel;

import java.util.List;

public record SearchedDocumentResponse(String uuid, String name, List<LabelModel> labels) {

}
