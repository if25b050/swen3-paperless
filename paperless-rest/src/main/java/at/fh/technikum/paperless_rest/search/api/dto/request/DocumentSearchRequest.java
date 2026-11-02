package at.fh.technikum.paperless_rest.search.api.dto.request;

public record DocumentSearchRequest(String search, int page, int pageSize) {
}
