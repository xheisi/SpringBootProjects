package org.test.cleancode.repository;

import org.springframework.stereotype.Repository;
import org.test.cleancode.domain.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CarRepositoryMessy {
    private final Map<Long, Car> carsById = new HashMap<>();
    private long nextId = 1L;

    public Car save(Car car) {
        Long carId = car.getId();
        if (carId == null) {
            carId = nextId++;
        }
        Car savedCar = new Car(carId, car.getOwnerName(), car.getPlateNumber(), car.getModel());
        carsById.put(carId, savedCar);
        return savedCar;
    }

    public List<Car> findAll() {
        return new ArrayList<>(carsById.values());
    }

    public Optional<Car> findByPlateNumber(String plateNumber) {
        return carsById.values()
                .stream()
                .filter(car -> car.getPlateNumber().equalsIgnoreCase(plateNumber))
                .findFirst();
    }
}