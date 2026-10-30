package at.fh.technikum.paperless_common.business.exception;

public class S3ObjectNotFoundException extends RuntimeException {
    public S3ObjectNotFoundException(String message) {
        super(message);
    }
}
