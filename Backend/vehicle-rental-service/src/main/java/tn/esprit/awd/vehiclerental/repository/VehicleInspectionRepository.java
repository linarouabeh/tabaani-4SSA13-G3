package tn.esprit.awd.vehiclerental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.awd.vehiclerental.entity.InspectionType;
import tn.esprit.awd.vehiclerental.entity.VehicleInspection;

public interface VehicleInspectionRepository extends JpaRepository<VehicleInspection, Long> {
    boolean existsByReservationIdAndInspectionType(Long reservationId, InspectionType type);
}
