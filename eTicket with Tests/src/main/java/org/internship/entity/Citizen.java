package org.internship.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "citizen")

@Getter@Setter
public class Citizen extends User {

    @OneToMany(mappedBy = "citizen")
    private List<Vehicle> vehicles;
}
