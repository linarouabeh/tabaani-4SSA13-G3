package com.exemple.reclamation.controller;

import com.exemple.reclamation.entity.Reclamation;
import com.exemple.reclamation.service.ReclamationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {

    private final ReclamationService reclamationService;

    public ReclamationController(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }

    @PostMapping
    public ResponseEntity<Reclamation> create(@RequestBody Reclamation reclamation) {
        Reclamation createdReclamation = reclamationService.create(reclamation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReclamation);
    }

    @GetMapping
    public ResponseEntity<List<Reclamation>> findAll() {
        return ResponseEntity.ok(reclamationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> findById(@PathVariable Long id) {
        return reclamationService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reclamation> update(
            @PathVariable Long id,
            @RequestBody Reclamation reclamation) {
        return reclamationService.update(id, reclamation)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!reclamationService.delete(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
