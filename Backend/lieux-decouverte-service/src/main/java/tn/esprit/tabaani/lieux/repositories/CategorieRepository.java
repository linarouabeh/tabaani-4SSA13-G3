package tn.esprit.tabaani.lieux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tabaani.lieux.entities.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
}