package tn.esprit.awd.vehiclerental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter @Setter @NoArgsConstructor
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String make;
    @Column(nullable = false) private String model;
    @Column(nullable = false, unique = true) private String registrationNumber;
    private String bodyType;
    private Integer seatingCapacity;
    private String transmissionType;
    private String fuelType;
    @Column(nullable = false) private String city;
    @Column(nullable = false, precision = 10, scale = 2) private BigDecimal pricePerDay;
    @Column(precision = 10, scale = 2) private BigDecimal depositAmount;
    /** Référence logique vers le MS Utilisateurs (pas de FK inter-services) */
    @Column(nullable = false) private Long ownerId;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private VehicleStatus status = VehicleStatus.DISPONIBLE;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleAvailability> availabilities = new ArrayList<>();
    @OneToMany(mappedBy = "vehicle")
    private List<RentalReservation> reservations = new ArrayList<>();
}
