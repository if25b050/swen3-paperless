package at.fh.technikum.paperless_rest.common.exception;

public class ModelValidationFailedException extends RuntimeException {
    public ModelValidationFailedException(String message) {
        super(message);
    }
}
