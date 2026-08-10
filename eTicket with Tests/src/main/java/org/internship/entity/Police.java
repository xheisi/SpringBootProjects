package org.internship.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "police")

@Getter
@Setter
public class Police extends User {

    private String badgeNumber;
    @OneToMany(mappedBy = "officer")
    private List<Fine> fines;
}
