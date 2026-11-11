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
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;

/**
 * Réservation d'une place sur un trajet par un passager. Une réservation
 * correspond exactement à une place.
 *
 * Relation UML : Trajet (1) -- (0..*) ReservationTrajet. Côté possédant (écriture)
 * de la clé étrangère {@code trajet_id} via l'association {@link ManyToOne} vers
 * {@link Trajet}. L'attribut UML {@code trajetId} est implémenté sous forme
 * d'association JPA (pas de second champ {@code Long} redondant) et reste
 * accessible en lecture seule via {@link #getTrajetId()}.
 *
 * {@code passagerId} référence le microservice externe Utilisateurs : simple
 * {@code Long} sans entité locale ni clé étrangère inter-base.
 */
@Entity
@Table(name = "reservations_trajet")
public class ReservationTrajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    @Column(name = "passager_id", nullable = false)
    private Long passagerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut;

    @Column(name = "date_reservation", nullable = false)
    private LocalDateTime dateReservation;

    public ReservationTrajet() {
    }

    public ReservationTrajet(Trajet trajet, Long passagerId, StatutReservation statut, LocalDateTime dateReservation) {
        this.trajet = trajet;
        this.passagerId = passagerId;
        this.statut = statut;
        this.dateReservation = dateReservation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    /** Identifiant du trajet (clé étrangère trajet_id) — accès en lecture seule. */
    @Transient
    public Long getTrajetId() {
        return trajet != null ? trajet.getId() : null;
    }

    public Long getPassagerId() {
        return passagerId;
    }

    public void setPassagerId(Long passagerId) {
        this.passagerId = passagerId;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }
}
