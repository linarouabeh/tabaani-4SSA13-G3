package tn.esprit.tabaani.voyages.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "validation_pedagogique")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationPedagogique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 0..1 cote validation : un voyage a au plus une validation
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voyage_id", nullable = false, unique = true)
    @JsonIgnore
    private Voyage voyage;

    // Reference vers le microservice Utilisateurs (role enseignant)
    private Long enseignantId;

    @Column(nullable = false)
    private String decision;    // EN_ATTENTE, VALIDEE, REFUSEE

    @Column(length = 2000)
    private String commentaire;

    private LocalDateTime dateDecision;

    public void valider() {
        this.decision = "VALIDEE";
        this.dateDecision = LocalDateTime.now();
    }

    public void refuser() {
        this.decision = "REFUSEE";
        this.dateDecision = LocalDateTime.now();
    }
}
