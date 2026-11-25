package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplace.entity.GearItem;
import java.util.List;

public interface GearItemRepository extends JpaRepository<GearItem, Long> {
    List<GearItem> findByNameContainingIgnoreCase(String keyword);
}