package com.rescueme.controller;

import com.rescueme.model.EmergencyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EmergencyResponse> validation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream().findFirst()
                .map(error -> error.getDefaultMessage()).orElse("Please provide a valid emergency message.");
        return ResponseEntity.badRequest().body(EmergencyResponse.failure(message));
    }
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<EmergencyResponse> malformedJson() {
        return ResponseEntity.badRequest().body(EmergencyResponse.failure("Request body must be valid JSON."));
    }
}
