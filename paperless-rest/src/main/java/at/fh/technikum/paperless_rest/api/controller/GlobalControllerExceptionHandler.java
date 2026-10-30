package at.fh.technikum.paperless_rest.api.controller;

import at.fh.technikum.paperless_rest.business.exception.DALObjectNotFoundException;
import at.fh.technikum.paperless_common.business.exception.InvalidUUIDException;
import at.fh.technikum.paperless_rest.business.exception.ModelValidationFailedException;
import at.fh.technikum.paperless_common.business.exception.S3ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleConflict(DataIntegrityViolationException ex) {
        log.error(ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "There was a conflict when inserting.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleInvalidMessage(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "The request was invalid.");
    }

    @ExceptionHandler(ModelValidationFailedException.class)
    public ProblemDetail handleValidationFailed(ModelValidationFailedException ex) {
        log.warn(ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(InvalidUUIDException.class)
    public ProblemDetail handleInvalidUUID(InvalidUUIDException ex) {
        log.warn(ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DALObjectNotFoundException.class)
    public ProblemDetail handleNotFound(DALObjectNotFoundException ex) {
        log.warn(ex.getMessage());
        return ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(S3ObjectNotFoundException.class)
    public ProblemDetail handleNotFound(S3ObjectNotFoundException ex) {
        log.warn(ex.getMessage());
        return ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail exceptionFallback(HttpServletRequest req, Exception ex) {
        log.error("Request: {} raised {}", req.getRequestURL(), ex.getMessage(), ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong.");
    }
}
