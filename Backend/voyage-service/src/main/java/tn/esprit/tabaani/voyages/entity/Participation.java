package tn.esprit.tabaani.voyages.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "participation",
       uniqueConstraints = @UniqueConstraint(columnNames = {"voyage_id", "utilisateurId"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voyage_id", nullable = false)
    @JsonIgnore
    private Voyage voyage;

    // Reference vers le microservice Utilisateurs
    @Column(nullable = false)
    private Long utilisateurId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateInscription;

    @Column(nullable = false)
    private String statut;      // INSCRIT, CONFIRME, ANNULE

    public void inscrire() {
        this.statut = "INSCRIT";
    }

    public void annulerParticipation() {
        this.statut = "ANNULE";
    }
}
