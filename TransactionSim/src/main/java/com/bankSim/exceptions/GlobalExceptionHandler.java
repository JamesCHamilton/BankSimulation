package com.bankSim.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({InsufficientFundsException.class})
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException ex) {
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMessage());
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
    }

    @ExceptionHandler({UnauthorizedAccessException.class})
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ex.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ex.getMessage());
    }   
}
