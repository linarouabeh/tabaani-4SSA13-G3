package tn.esprit.tabaani.lieux.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tabaani.lieux.entities.Categorie;
import tn.esprit.tabaani.lieux.repositories.CategorieRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    private final CategorieRepository categorieRepository;

    public CategorieController(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @PostMapping
    public ResponseEntity<Categorie> ajouterCategorie(
            @Valid @RequestBody Categorie categorie) {

        categorie.setId(null);

        Categorie nouvelleCategorie =
                categorieRepository.save(categorie);

        return ResponseEntity
                .created(URI.create(
                        "/api/categories/" + nouvelleCategorie.getId()
                ))
                .body(nouvelleCategorie);
    }

    @GetMapping
    public List<Categorie> afficherCategories() {
        return categorieRepository.findAll();
    }
}