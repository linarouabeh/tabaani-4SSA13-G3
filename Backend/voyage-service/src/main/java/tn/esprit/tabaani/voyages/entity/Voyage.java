package tn.esprit.tabaani.voyages.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "voyage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Voyage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime dateDepart;

    @Column(nullable = false)
    private LocalDateTime dateRetour;

    @Column(nullable = false)
    private Integer nombrePlaces;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(nullable = false)
    private String type;        // ROADTRIP ou SORTIE_PEDAGOGIQUE

    @Column(nullable = false)
    private String statut;      // BROUILLON, PUBLIE, ANNULE

    // Reference vers le microservice Utilisateurs : un simple id, pas de relation JPA
    @Column(nullable = false)
    private Long organisateurId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateCreation;

    // 1 Voyage contient 0..* Etape
    @OneToMany(mappedBy = "voyage", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordre ASC")
    private List<Etape> etapes = new ArrayList<>();

    // 1 Voyage gere 0..* Participation
    @OneToMany(mappedBy = "voyage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations = new ArrayList<>();

    // 1 Voyage envoie 0..* Invitation
    @OneToMany(mappedBy = "voyage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invitation> invitations = new ArrayList<>();

    // 1 Voyage necessite 0..1 ValidationPedagogique
    @OneToOne(mappedBy = "voyage", cascade = CascadeType.ALL, orphanRemoval = true)
    private ValidationPedagogique validationPedagogique;

    public void creer() {
        this.statut = "BROUILLON";
    }

    public void publier() {
        this.statut = "PUBLIE";
    }

    public void annuler() {
        this.statut = "ANNULE";
    }
}
