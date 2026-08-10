package org.test.cleancode.service;

import org.springframework.stereotype.Service;
import org.test.cleancode.domain.Car;
import org.test.cleancode.dto.CarResponse;
import org.test.cleancode.dto.RegisterCarRequest;
import org.test.cleancode.exception.CarPlateAlreadyExistsException;
import org.test.cleancode.exception.InvalidCarRegistrationException;
import org.test.cleancode.repository.CarRepositoryMessy;

@Service
public class CarServiceMessy {
    private final CarRepositoryMessy carRepositoryMessy;

    public CarServiceMessy(CarRepositoryMessy carRepositoryMessy) {
        this.carRepositoryMessy = carRepositoryMessy;
    }

    public CarResponse registerCar(RegisterCarRequest request) {
        validateRequest(request);

        String normalizedPlate = request.getPlateNumber().trim().toUpperCase();
        carRepositoryMessy.findByPlateNumber(normalizedPlate).ifPresent(existingCar -> {
            throw new CarPlateAlreadyExistsException("Plate number already exists: " + normalizedPlate);
        });

        Car carToSave = new Car(null, request.getOwnerName().trim(), normalizedPlate, request.getModel().trim());
        Car savedCar = carRepositoryMessy.save(carToSave);

        return new CarResponse(savedCar.getId(), savedCar.getOwnerName(), savedCar.getPlateNumber(), savedCar.getModel());
    }

    private void validateRequest(RegisterCarRequest request) {
        if (request == null) {
            throw new InvalidCarRegistrationException("Request is required");
        }
        if (isBlank(request.getOwnerName())) {
            throw new InvalidCarRegistrationException("Owner name is required");
        }
        if (isBlank(request.getPlateNumber())) {
            throw new InvalidCarRegistrationException("Plate number is required");
        }
        if (isBlank(request.getModel())) {
            throw new InvalidCarRegistrationException("Model is required");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}