package tn.esprit.awd.vehiclerental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_inspections")
@Getter @Setter @NoArgsConstructor
public class VehicleInspection {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private InspectionType inspectionType;
    private Integer odometerReading;
    @Column(length = 1000) private String observations;
    @Column(length = 1000) private String damageReport;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private InspectionResult inspectionResult;
    @Column(nullable = false) private LocalDateTime inspectedAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id")
    private RentalReservation reservation;
}
