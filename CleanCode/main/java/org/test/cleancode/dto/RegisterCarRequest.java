package org.test.cleancode.dto;

public class RegisterCarRequest {
    private String ownerName;
    private String plateNumber;
    private String model;

    public RegisterCarRequest() {
    }

    public RegisterCarRequest(String ownerName, String plateNumber, String model) {
        this.ownerName = ownerName;
        this.plateNumber = plateNumber;
        this.model = model;
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