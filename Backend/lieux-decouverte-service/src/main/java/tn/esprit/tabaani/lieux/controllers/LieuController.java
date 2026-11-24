package tn.esprit.tabaani.lieux.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tabaani.lieux.entities.Lieu;
import tn.esprit.tabaani.lieux.services.ILieuService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lieux")
public class LieuController {

    private final ILieuService lieuService;

    public LieuController(ILieuService lieuService) {
        this.lieuService = lieuService;
    }

    @PostMapping
    public ResponseEntity<Lieu> ajouterLieu(
            @Valid @RequestBody Lieu lieu) {

        Lieu nouveauLieu = lieuService.ajouterLieu(lieu);

        URI emplacement = URI.create(
                "/api/lieux/" + nouveauLieu.getId()
        );

        return ResponseEntity
                .created(emplacement)
                .body(nouveauLieu);
    }

    @GetMapping
    public List<Lieu> afficherLieux() {
        return lieuService.afficherLieux();
    }

    @GetMapping("/{id}")
    public Lieu afficherLieuParId(@PathVariable("id") Long id) {
        return lieuService.afficherLieuParId(id);
    }

    @PutMapping("/{id}")
    public Lieu modifierLieu(
            @PathVariable("id") Long id,
            @Valid @RequestBody Lieu lieu) {

        return lieuService.modifierLieu(id, lieu);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerLieu(
            @PathVariable("id") Long id) {

        lieuService.supprimerLieu(id);

        return ResponseEntity.noContent().build();
    }
}