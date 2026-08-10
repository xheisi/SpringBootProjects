package org.test.cleancode.exception;

public class CarPlateAlreadyExistsException extends RuntimeException {
    public CarPlateAlreadyExistsException(String message) {
        super(message);
    }
}