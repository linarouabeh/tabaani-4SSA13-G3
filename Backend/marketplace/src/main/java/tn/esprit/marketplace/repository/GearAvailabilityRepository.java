package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplace.entity.GearAvailability;

public interface GearAvailabilityRepository extends JpaRepository<GearAvailability, Long> {
}