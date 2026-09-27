package at.fh.technikum.paperless_rest.api.dto.request;

public record DocumentSearchRequest(String search, int page, int pageSize) {
}
