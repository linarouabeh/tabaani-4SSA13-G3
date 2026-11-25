package tn.esprit.marketplace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class GearReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long renterId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "gear_id")
    private GearItem gear;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}