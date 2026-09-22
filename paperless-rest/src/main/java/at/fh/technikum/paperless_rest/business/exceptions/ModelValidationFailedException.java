package at.fh.technikum.paperless_rest.business.exceptions;

public class ModelValidationFailedException extends RuntimeException {
    public ModelValidationFailedException(String message) {
        super(message);
    }
}
