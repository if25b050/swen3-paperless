package at.fh.technikum.paperless_rest.presentation.dto.request;

import java.util.List;

public record DocumentUpdateRequest(String name, List<String> labels) {
}
