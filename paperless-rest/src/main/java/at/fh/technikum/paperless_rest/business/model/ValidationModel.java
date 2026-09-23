package at.fh.technikum.paperless_rest.business.model;

import at.fh.technikum.paperless_rest.business.exceptions.ModelValidationFailedException;
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
