package at.fh.technikum.paperless_rest.business.exception;

public class DALObjectNotFoundException extends RuntimeException {
    public DALObjectNotFoundException(String message) {
        super(message);
    }
}
