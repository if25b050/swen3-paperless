package at.fh.technikum.paperless_common.business.exception;

public class InvalidUUIDException extends RuntimeException {
    public InvalidUUIDException(String uuid) {
        super(String.format("\"%s\" is not a valid UUID.", uuid));
    }
}
