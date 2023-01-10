package com.example.pcswebserver.web;

import com.example.pcswebserver.exception.*;
import com.example.pcswebserver.web.payload.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class, StoreDirectoryNotFoundException.class})
    public ResponseEntity<ExceptionDetails> handleNotFound(RuntimeException exception, WebRequest request) {
        return handleGeneric(exception, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionDetails> handleConflict(RuntimeException exception, WebRequest request) {
        return handleGeneric(exception, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ExceptionDetails> handleForbidden(RuntimeException exception, WebRequest request) {
        return handleGeneric(exception, request, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ExceptionDetails> handleGeneric(RuntimeException exception,
                                                           WebRequest request,
                                                           HttpStatus status) {
        return new ResponseEntity<>(
                new ExceptionDetails(
                        exception.getMessage(),
                        request.getDescription(false),
                        LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))),
                status);
    }


}