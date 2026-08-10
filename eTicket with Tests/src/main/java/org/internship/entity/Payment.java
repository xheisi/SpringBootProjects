package org.internship.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment")

@Getter@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private LocalDate paymentDate;
    private double amount;

    @OneToOne
    @JoinColumn(name = "fine_id")
    private Fine fine;
}
