package tn.esprit.awd.vehiclerental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rental_reservations")
@Getter @Setter @NoArgsConstructor
public class RentalReservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private LocalDateTime pickupDateTime;
    @Column(nullable = false) private LocalDateTime returnDateTime;
    @Column(precision = 10, scale = 2) private BigDecimal totalAmount;
    @Column(precision = 10, scale = 2) private BigDecimal depositAmount;
    /** Référence logique vers le MS Utilisateurs */
    @Column(nullable = false) private Long customerId;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.EN_ATTENTE_CAUTION;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleInspection> inspections = new ArrayList<>();
}
