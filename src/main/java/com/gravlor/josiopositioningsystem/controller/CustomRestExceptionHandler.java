package com.gravlor.josiopositioningsystem.controller;

import com.gravlor.josiopositioningsystem.exception.ApiError;
import com.gravlor.josiopositioningsystem.exception.GateInvalidDurationException;
import com.gravlor.josiopositioningsystem.exception.MapAlreadyExistsException;
import com.gravlor.josiopositioningsystem.exception.MapNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ MapAlreadyExistsException.class })
    public ResponseEntity<Object> handleMapAlreadyExistsException(MapAlreadyExistsException ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ MapNotFoundException.class })
    public ResponseEntity<Object> handleMapNotFoundException(MapNotFoundException ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ GateInvalidDurationException.class })
    public ResponseEntity<Object> handleMapAlreadyExistsException(GateInvalidDurationException ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
