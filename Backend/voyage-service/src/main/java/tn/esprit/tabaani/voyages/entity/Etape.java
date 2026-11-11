package tn.esprit.tabaani.voyages.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "etape")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Etape {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // voyageId du diagramme -> colonne voyage_id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voyage_id", nullable = false)
    @JsonIgnore
    private Voyage voyage;

    @Column(nullable = false)
    private String nom;

    @Column(length = 2000)
    private String description;

    private String adresse;

    @Column(nullable = false)
    private Integer ordre;

    private LocalDateTime dateArrivee;

    private LocalDateTime dateDepart;
}
