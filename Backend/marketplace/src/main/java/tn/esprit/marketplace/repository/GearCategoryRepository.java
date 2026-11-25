package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplace.entity.GearCategory;

public interface GearCategoryRepository extends JpaRepository<GearCategory, Long> {
}