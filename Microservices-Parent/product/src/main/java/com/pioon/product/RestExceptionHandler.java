package com.pioon.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RestMessage> handleRuntimeException(RuntimeException exception) {
        String errorMessage = exception.getMessage();
        RestMessage restMessage = new RestMessage(errorMessage);
        return new ResponseEntity<>(restMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestMessage> handleUnexpectedException(RuntimeException exception) {
        String errorMessage = "Unexpected error";
        RestMessage restMessage = new RestMessage(errorMessage);
        return new ResponseEntity<>(restMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
