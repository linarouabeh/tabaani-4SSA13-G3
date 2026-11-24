package tn.esprit.tabaani.lieux.repositories;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tabaani.lieux.entities.Lieu;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface LieuRepository extends JpaRepository<Lieu, Long> {

    @Override
    @EntityGraph(attributePaths = {"categorie", "tags"})
    List<Lieu> findAll();

    @Override
    @EntityGraph(attributePaths = {"categorie", "tags"})
    Optional<Lieu> findById(Long id);
}