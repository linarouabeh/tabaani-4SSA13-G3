package tn.esprit.awd.vehiclerental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.awd.vehiclerental.entity.RentalReservation;
import tn.esprit.awd.vehiclerental.entity.ReservationStatus;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface RentalReservationRepository extends JpaRepository<RentalReservation, Long> {
    List<RentalReservation> findByCustomerId(Long customerId);

    @Query("""
        select count(r) > 0 from RentalReservation r
        where r.vehicle.id = :vehicleId and r.status in :active
          and r.pickupDateTime < :end and r.returnDateTime > :start
        """)
    boolean existsOverlap(@Param("vehicleId") Long vehicleId,
                          @Param("active") Collection<ReservationStatus> active,
                          @Param("start") LocalDateTime start,
                          @Param("end") LocalDateTime end);

    boolean existsByVehicleIdAndStatusIn(Long vehicleId, Collection<ReservationStatus> statuses);
}
