package at.fh.technikum.paperless_rest.business.model;

import java.util.List;

public record DocumentUpdateModel(int id, String name, List<String> labels) {
}
