package at.fh.technikum.paperless_rest.presentation.dto.request;

public record DocumentCreateRequest(String name, byte[] file) {
}
