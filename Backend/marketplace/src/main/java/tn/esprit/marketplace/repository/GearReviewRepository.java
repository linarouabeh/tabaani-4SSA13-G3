package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplace.entity.GearReview;

public interface GearReviewRepository extends JpaRepository<GearReview, Long> {
}