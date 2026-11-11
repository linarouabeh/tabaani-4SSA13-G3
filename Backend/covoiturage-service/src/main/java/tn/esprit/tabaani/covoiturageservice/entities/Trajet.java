package tn.esprit.tabaani.covoiturageservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Trajet proposé par un conducteur.
 *
 * Relation UML : Conducteur (1) -- (0..*) Trajet. Côté possédant (écriture) de la
 * clé étrangère {@code conducteur_id} via l'association {@link ManyToOne} vers
 * {@link Conducteur}. L'attribut UML {@code conducteurId} est implémenté sous
 * forme d'association JPA (et non d'un second champ {@code Long} redondant), ce
 * qui évite de mapper deux propriétés modifiables sur la même colonne. L'identifiant
 * reste accessible en lecture seule via {@link #getConducteurId()}.
 *
 * L'attribut UML {@code conducteurId} est exposé via un getter dérivé (non persisté).
 */
@Entity
@Table(name = "trajets")
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conducteur_id", nullable = false)
    private Conducteur conducteur;

    @Column(nullable = false)
    private String depart;

    @Column(nullable = false)
    private String destination;

    @Column(name = "date_depart", nullable = false)
    private LocalDateTime dateDepart;

    @Column(name = "places_disponibles", nullable = false)
    private Integer placesDisponibles;

    @Column(name = "prix_par_place", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixParPlace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTrajet statut;

    /** Côté inverse (lecture seule) de la relation Trajet (1) -- (0..*) ReservationTrajet. */
    @OneToMany(mappedBy = "trajet", fetch = FetchType.LAZY)
    private List<ReservationTrajet> reservations = new ArrayList<>();

    /** Côté inverse (lecture seule) de la relation Trajet (1) -- (0..*) EvaluationTrajet. */
    @OneToMany(mappedBy = "trajet", fetch = FetchType.LAZY)
    private List<EvaluationTrajet> evaluations = new ArrayList<>();

    public Trajet() {
    }

    public Trajet(Conducteur conducteur, String depart, String destination, LocalDateTime dateDepart,
                  Integer placesDisponibles, BigDecimal prixParPlace, StatutTrajet statut) {
        this.conducteur = conducteur;
        this.depart = depart;
        this.destination = destination;
        this.dateDepart = dateDepart;
        this.placesDisponibles = placesDisponibles;
        this.prixParPlace = prixParPlace;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conducteur getConducteur() {
        return conducteur;
    }

    public void setConducteur(Conducteur conducteur) {
        this.conducteur = conducteur;
    }

    /** Identifiant du conducteur (clé étrangère conducteur_id) — accès en lecture seule. */
    @Transient
    public Long getConducteurId() {
        return conducteur != null ? conducteur.getId() : null;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Integer getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(Integer placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    public BigDecimal getPrixParPlace() {
        return prixParPlace;
    }

    public void setPrixParPlace(BigDecimal prixParPlace) {
        this.prixParPlace = prixParPlace;
    }

    public StatutTrajet getStatut() {
        return statut;
    }

    public void setStatut(StatutTrajet statut) {
        this.statut = statut;
    }

    public List<ReservationTrajet> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationTrajet> reservations) {
        this.reservations = reservations;
    }

    public List<EvaluationTrajet> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<EvaluationTrajet> evaluations) {
        this.evaluations = evaluations;
    }
}
