package tn.esprit.marketplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.marketplace.entity.GearItem;
import tn.esprit.marketplace.repository.GearItemRepository;
import java.util.List;

@RestController
@RequestMapping("/api/gears")
public class GearController {

    private final GearItemRepository repo;

    public GearController(GearItemRepository repo) {
        this.repo = repo;
    }

    // Lister et rechercher : GET /api/gears  ou  GET /api/gears?keyword=tente
    @GetMapping
    public List<GearItem> list(@RequestParam(required = false) String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return repo.findAll();
        }
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GearItem> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GearItem> create(@RequestBody GearItem g) {
        g.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(g));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GearItem> update(@PathVariable Long id, @RequestBody GearItem g) {
        return repo.findById(id).map(old -> {
            old.setName(g.getName());
            old.setDescription(g.getDescription());
            old.setPricePerDay(g.getPricePerDay());
            old.setDepositAmount(g.getDepositAmount());
            old.setStatus(g.getStatus());
            old.setCategory(g.getCategory());
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