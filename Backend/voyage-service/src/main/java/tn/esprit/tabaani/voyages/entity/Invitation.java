package tn.esprit.tabaani.voyages.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voyage_id", nullable = false)
    @JsonIgnore
    private Voyage voyage;

    // References vers le microservice Utilisateurs
    @Column(nullable = false)
    private Long expediteurId;

    @Column(nullable = false)
    private Long destinataireId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateEnvoi;

    @Column(nullable = false)
    private String statut;      // EN_ATTENTE, ACCEPTEE, REFUSEE

    public void envoyer() {
        this.statut = "EN_ATTENTE";
    }

    public void accepter() {
        this.statut = "ACCEPTEE";
    }

    public void refuser() {
        this.statut = "REFUSEE";
    }
}
