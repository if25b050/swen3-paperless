package at.fh.technikum.paperless_rest.presentation.controller;

import at.fh.technikum.paperless_rest.business.exceptions.ModelValidationFailedException;
import at.fh.technikum.paperless_rest.business.exceptions.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleConflict(DataIntegrityViolationException ex) {
        log.error(ex.getMessage(), ex);
        // TODO more specific Message?
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ModelValidationFailedException.class)
    public ProblemDetail handleValidationFailed(ModelValidationFailedException ex) {
        log.warn(ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ProblemDetail handleNotFound(ObjectNotFoundException ex) {
        log.warn(ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail exceptionFallback(HttpServletRequest req, Exception ex) {
        log.error("Request: {} raised {}", req.getRequestURL(), ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }
}
