package at.fh.technikum.paperless_rest.api.dto.request;

public record DocumentCreateRequest(String name, byte[] file) {
}
