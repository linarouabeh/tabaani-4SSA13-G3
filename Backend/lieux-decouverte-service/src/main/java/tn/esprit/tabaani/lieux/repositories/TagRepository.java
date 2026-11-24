package tn.esprit.tabaani.lieux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tabaani.lieux.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}