package at.fh.technikum.paperless_rest.document.api.dto.response;

import at.fh.technikum.paperless_rest.label.business.model.LabelModel;

import java.util.List;

public record DocumentResponse(String uuid, String name, List<LabelModel> labels) {

}
