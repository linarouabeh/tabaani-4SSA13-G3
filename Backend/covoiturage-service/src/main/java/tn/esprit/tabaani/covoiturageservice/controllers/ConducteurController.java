package tn.esprit.tabaani.covoiturageservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.tabaani.covoiturageservice.entities.Conducteur;
import tn.esprit.tabaani.covoiturageservice.services.ConducteurService;

import java.util.List;

@RestController
@RequestMapping("/api/conducteurs")
public class ConducteurController {

    private final ConducteurService conducteurService;

    public ConducteurController(ConducteurService conducteurService) {
        this.conducteurService = conducteurService;
    }

    // CREATE - Ajouter un conducteur
    @PostMapping
    public ResponseEntity<Conducteur> ajouter(
            @RequestBody Conducteur conducteur) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(conducteurService.ajouter(conducteur));
    }

    // READ - Afficher tous les conducteurs
    @GetMapping
    public List<Conducteur> lister() {
        return conducteurService.lister();
    }

    // READ - Rechercher par ID
    @GetMapping("/{id}")
    public Conducteur trouverParId(@PathVariable Long id) {
        return conducteurService.trouverParId(id);
    }

    // UPDATE - Modifier un conducteur
    @PutMapping("/{id}")
    public Conducteur modifier(
            @PathVariable Long id,
            @RequestBody Conducteur conducteur) {

        return conducteurService.modifier(id, conducteur);
    }

    // DELETE - Supprimer un conducteur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        conducteurService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
