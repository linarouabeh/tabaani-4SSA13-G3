package tn.esprit.awd.vehiclerental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.awd.vehiclerental.entity.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByRegistrationNumberIgnoreCase(String registrationNumber);
    List<Vehicle> findByCityIgnoreCase(String city);
    List<Vehicle> findByStatus(VehicleStatus status);
    List<Vehicle> findByCityIgnoreCaseAndStatus(String city, VehicleStatus status);

    /** Véhicules disponibles sur [start, end] : pas de réservation active qui chevauche */
    @Query("""
        select v from Vehicle v
        where v.status = :status
          and not exists (
            select r.id from RentalReservation r
            where r.vehicle = v and r.status in :active
              and r.pickupDateTime < :end and r.returnDateTime > :start)
        """)
    List<Vehicle> findFreeBetween(@Param("status") VehicleStatus status,
                                  @Param("active") Collection<ReservationStatus> active,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);
}
