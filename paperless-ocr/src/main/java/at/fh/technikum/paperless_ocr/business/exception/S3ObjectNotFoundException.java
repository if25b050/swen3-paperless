package at.fh.technikum.paperless_ocr.business.exception;

public class S3ObjectNotFoundException extends RuntimeException {
    public S3ObjectNotFoundException(String message) {
        super(message);
    }
}
