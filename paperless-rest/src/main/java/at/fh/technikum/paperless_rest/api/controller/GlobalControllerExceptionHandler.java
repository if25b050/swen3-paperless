package at.fh.technikum.paperless_rest.api.controller;

import at.fh.technikum.paperless_rest.business.exception.ModelValidationFailedException;
import at.fh.technikum.paperless_rest.business.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleConflict(DataIntegrityViolationException ex) {
        log.error(ex.getMessage(), ex);
        // TODO more specific Message?
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(ModelValidationFailedException.class)
    public String handleValidationFailed(ModelValidationFailedException ex) {
        log.warn(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(ObjectNotFoundException.class)
    public String handleNotFound(ObjectNotFoundException ex) {
        log.warn(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Exception.class)
    public String exceptionFallback(Exception ex) {
        log.error(ex.getMessage(), ex);
        return "Some error occurred.";
    }

}
