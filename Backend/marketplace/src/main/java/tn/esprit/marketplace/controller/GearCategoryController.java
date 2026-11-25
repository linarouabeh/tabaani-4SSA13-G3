package tn.esprit.marketplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.marketplace.entity.GearCategory;
import tn.esprit.marketplace.repository.GearCategoryRepository;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class GearCategoryController {

    private final GearCategoryRepository repo;

    public GearCategoryController(GearCategoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<GearCategory> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GearCategory> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GearCategory> create(@RequestBody GearCategory c) {
        c.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GearCategory> update(@PathVariable Long id, @RequestBody GearCategory c) {
        return repo.findById(id).map(old -> {
            old.setName(c.getName());
            old.setDescription(c.getDescription());
            return ResponseEntity.ok(repo.save(old));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}