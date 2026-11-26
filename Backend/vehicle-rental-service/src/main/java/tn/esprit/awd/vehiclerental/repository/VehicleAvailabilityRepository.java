package tn.esprit.awd.vehiclerental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.awd.vehiclerental.entity.VehicleAvailability;
import java.time.LocalDate;
import java.util.List;

public interface VehicleAvailabilityRepository extends JpaRepository<VehicleAvailability, Long> {
    List<VehicleAvailability> findByVehicleId(Long vehicleId);

    /** Une période bloquée (isAvailable = false) qui chevauche [start, end] */
    @Query("""
        select count(a) > 0 from VehicleAvailability a
        where a.vehicle.id = :vehicleId and a.isAvailable = false
          and a.startDate <= :end and a.endDate >= :start
        """)
    boolean existsBlockedOverlap(@Param("vehicleId") Long vehicleId,
                                 @Param("start") LocalDate start,
                                 @Param("end") LocalDate end);
}
