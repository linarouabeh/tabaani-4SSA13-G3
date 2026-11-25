
package tn.esprit.tabaani.covoiturageservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tabaani.covoiturageservice.entities.Conducteur;

public interface ConducteurRepository
        extends JpaRepository<Conducteur, Long> {
}
