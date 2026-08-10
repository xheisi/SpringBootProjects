package org.internship.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "fine")

@Getter@Setter
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private double amount;
    private LocalDate date;
    @Column(length = 100)
    private String reason;

    @Enumerated(EnumType.STRING)
    private FineStatus status = FineStatus.UNPAID;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "officer_id")
    private Police officer;

    @OneToOne(mappedBy = "fine")
    private Payment payment;

}
