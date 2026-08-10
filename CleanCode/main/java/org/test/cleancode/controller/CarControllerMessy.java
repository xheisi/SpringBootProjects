package org.test.cleancode.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.test.cleancode.dto.CarResponse;
import org.test.cleancode.dto.RegisterCarRequest;
import org.test.cleancode.service.CarServiceMessy;

@RestController
@RequestMapping("/cars")
public class CarControllerMessy {

    private final CarServiceMessy carServiceMessy;

    public CarControllerMessy(CarServiceMessy carServiceMessy) {
        this.carServiceMessy = carServiceMessy;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CarResponse registerCar(@RequestBody RegisterCarRequest request) {
        return carServiceMessy.registerCar(request);
    }
}