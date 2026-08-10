package org.internship.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "vechile")

@Getter
@Setter
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String plate;
    private String brand;
    private String model;

    @ManyToOne
    @JoinColumn(name = "citizen_id")
    private Citizen citizen;

    @OneToMany(mappedBy = "vehicle")
    private List<Fine> fines;
}
