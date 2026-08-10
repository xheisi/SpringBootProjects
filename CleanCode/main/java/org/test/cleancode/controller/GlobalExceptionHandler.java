package org.test.cleancode.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.test.cleancode.exception.CarPlateAlreadyExistsException;
import org.test.cleancode.exception.EmailAlreadyExistsException;
import org.test.cleancode.exception.InvalidCarRegistrationException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCarRegistrationException.class)
    public ResponseEntity<Map<String, String>> handleInvalidRegistration(InvalidCarRegistrationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Invalid registration",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExists(EmailAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Email already exists",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(InvalidCarRegistrationException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCarRegistration(InvalidCarRegistrationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Invalid registration",
                        "message", exception.getMessage()
                ));
    }

    @ExceptionHandler(CarPlateAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleCarPlateAlreadyExists(CarPlateAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Plate already exists",
                        "message", exception.getMessage()
                ));
    }
}
