package tn.esprit.tabaani.covoiturageservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Conducteur de covoiturage. {@code utilisateurId} référence le microservice
 * externe Utilisateurs : simple {@code Long} sans entité locale ni clé étrangère
 * inter-base.
 *
 * Relation UML : Conducteur (1) -- (0..*) Trajet. Le côté conducteur est le côté
 * inverse (lecture seule) de l'association : la clé étrangère {@code conducteur_id}
 * est possédée et écrite côté {@link Trajet}.
 */
@Entity
@Table(name = "conducteurs")
public class Conducteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "utilisateur_id", nullable = false, unique = true)
    private Long utilisateurId;

    @Column(name = "numero_permis", nullable = false)
    private String numeroPermis;

    @Column(nullable = false)
    private Boolean verifie = false;

    /** Côté inverse (lecture seule) de la relation Conducteur (1) -- (0..*) Trajet. */
    @OneToMany(mappedBy = "conducteur", fetch = FetchType.LAZY)
    private List<Trajet> trajets = new ArrayList<>();

    public Conducteur() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getNumeroPermis() {
        return numeroPermis;
    }

    public void setNumeroPermis(String numeroPermis) {
        this.numeroPermis = numeroPermis;
    }

    public Boolean getVerifie() {
        return verifie;
    }

    public void setVerifie(Boolean verifie) {
        this.verifie = verifie;
    }

    public List<Trajet> getTrajets() {
        return trajets;
    }

    public void setTrajets(List<Trajet> trajets) {
        this.trajets = trajets;
    }
}
