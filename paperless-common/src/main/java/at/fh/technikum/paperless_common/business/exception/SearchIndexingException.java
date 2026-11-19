package at.fh.technikum.paperless_common.business.exception;

public class SearchIndexingException extends RuntimeException {
    public SearchIndexingException(String message, Throwable cause) {
        super(message, cause);
    }
}
