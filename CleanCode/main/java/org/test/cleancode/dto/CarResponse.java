package org.test.cleancode.dto;

public class CarResponse {
    private final Long id;
    private final String ownerName;
    private final String plateNumber;
    private final String model;

    public CarResponse(Long id, String ownerName, String plateNumber, String model) {
        this.id = id;
        this.ownerName = ownerName;
        this.plateNumber = plateNumber;
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getModel() {
        return model;
    }
}