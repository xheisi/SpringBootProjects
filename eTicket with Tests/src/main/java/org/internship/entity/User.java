package org.internship.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)

@Getter
@Setter
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    private String surname;
}
