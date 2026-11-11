package tn.esprit.tabaani.covoiturageservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Évaluation d'un trajet par un passager l'ayant effectué. La note est
 * obligatoirement comprise entre 1 et 5 (validée au niveau service/DTO).
 *
 * Relation UML : Trajet (1) -- (0..*) EvaluationTrajet. Côté possédant (écriture)
 * de la clé étrangère {@code trajet_id} via l'association {@link ManyToOne} vers
 * {@link Trajet}. L'attribut UML {@code trajetId} est implémenté sous forme
 * d'association JPA (pas de second champ {@code Long} redondant) et reste
 * accessible en lecture seule via {@link #getTrajetId()}.
 *
 * {@code auteurId} référence le microservice externe Utilisateurs : simple
 * {@code Long} sans entité locale ni clé étrangère inter-base.
 */
@Entity
@Table(name = "evaluations_trajet")
public class EvaluationTrajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    @Column(name = "auteur_id", nullable = false)
    private Long auteurId;

    @Column(nullable = false)
    private Integer note;

    @Column(columnDefinition = "text")
    private String commentaire;

    @Column(name = "date_evaluation", nullable = false)
    private LocalDateTime dateEvaluation;

    public EvaluationTrajet() {
    }

    public EvaluationTrajet(Trajet trajet, Long auteurId, Integer note, String commentaire, LocalDateTime dateEvaluation) {
        this.trajet = trajet;
        this.auteurId = auteurId;
        this.note = note;
        this.commentaire = commentaire;
        this.dateEvaluation = dateEvaluation;
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

    public Long getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(Long auteurId) {
        this.auteurId = auteurId;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDateTime getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(LocalDateTime dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }
}
