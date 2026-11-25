package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplace.entity.GearReservation;

public interface GearReservationRepository extends JpaRepository<GearReservation, Long> {
}