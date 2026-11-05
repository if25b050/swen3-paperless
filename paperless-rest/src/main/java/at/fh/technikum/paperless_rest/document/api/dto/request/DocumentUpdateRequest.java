package at.fh.technikum.paperless_rest.document.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record DocumentUpdateRequest(
        @NotBlank @Pattern(regexp = "^[\\w,\\s-]+\\.pdf$", message = "The file name does not allow special characters.") String name,
        List<String> labels) {
}
