package at.fh.technikum.paperless_rest.document.api.dto.response;

import at.fh.technikum.paperless_rest.label.business.model.LabelModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

public record DocumentResponse(@UUID(allowNil = false) String uuid,
                               @NotBlank @Pattern(regexp = "^[\\w,\\s-]+\\.pdf$") String name,
                               List<LabelModel> labels) {

}
