package org.test.cleancode.exception;

public class InvalidCarRegistrationException extends RuntimeException {
    public InvalidCarRegistrationException(String message) {
        super(message);
    }
}