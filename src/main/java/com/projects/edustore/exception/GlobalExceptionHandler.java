package com.projects.edustore.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(value = ForbiddenActionException.class)
    public ResponseEntity<Object> forbiddenActionException(ForbiddenActionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(value = AuthenticationFailedException.class)
    public ResponseEntity<Object> authenticationFailedException(AuthenticationFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidJson(HttpMessageNotReadableException ex, HttpServletRequest request) {

        String path = request.getRequestURI();

        if ("/authenticate".equals(path)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Wrong JSON login request. Example: { \"userName\": \"johndoe\", \"password\": \"password123\" }");
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Wrong JSON request — please check your input format.");
    }



}
