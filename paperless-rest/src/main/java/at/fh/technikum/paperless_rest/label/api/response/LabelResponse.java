package at.fh.technikum.paperless_rest.label.api.response;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UUID;

public record LabelResponse(@UUID(allowNil = false) String uuid, @NotBlank String name) {
}
