package org.test.cleancode.domain;

public class Car {
    private Long id;
    private String ownerName;
    private String plateNumber;
    private String model;

    public Car(Long id, String ownerName, String plateNumber, String model) {
        this.id = id;
        this.ownerName = ownerName;
        this.plateNumber = plateNumber;
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
}
