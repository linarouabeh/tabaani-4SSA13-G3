package tn.esprit.awd.vehiclerental.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.awd.vehiclerental.dto.*;
import tn.esprit.awd.vehiclerental.entity.VehicleStatus;
import tn.esprit.awd.vehiclerental.service.VehicleService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vehicules")
@Tag(name = "Vehicules", description = "Catalogue des véhicules et disponibilités")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Lister et rechercher (ville, statut, période libre)")
    public List<VehicleResponse> search(@RequestParam(required = false) String city,
                                        @RequestParam(required = false) VehicleStatus status,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return service.search(city, status, from, to);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un véhicule")
    public VehicleResponse get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    @Operation(summary = "Ajouter une annonce de véhicule")
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest r) {
        VehicleResponse created = service.create(r);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une annonce")
    public VehicleResponse update(@PathVariable Long id, @Valid @RequestBody VehicleRequest r) {
        return service.update(id, r);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le statut (DISPONIBLE, MAINTENANCE, RETIRE...)")
    public VehicleResponse updateStatus(@PathVariable Long id, @Valid @RequestBody VehicleStatusRequest r) {
        return service.updateStatus(id, r.status());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Retirer une annonce")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/disponibilites")
    @Operation(summary = "Ajouter une période de disponibilité / blocage")
    public ResponseEntity<AvailabilityResponse> addAvailability(@PathVariable Long id, @Valid @RequestBody AvailabilityRequest r) {
        return ResponseEntity.status(201).body(service.addAvailability(id, r));
    }

    @GetMapping("/{id}/disponibilites")
    @Operation(summary = "Lister les disponibilités d'un véhicule")
    public List<AvailabilityResponse> availabilities(@PathVariable Long id) { return service.listAvailabilities(id); }
}
