package at.fh.technikum.paperless_rest.common.model;

import at.fh.technikum.paperless_rest.common.exception.ModelValidationFailedException;
import org.springframework.util.StringUtils;

public interface ValidationModel {

    String validationLogic();

    static void validate(ValidationModel i) {
        String s = i.validationLogic();
        if (StringUtils.hasText(s)) {
            throw new ModelValidationFailedException(s);
        }
    }
}
