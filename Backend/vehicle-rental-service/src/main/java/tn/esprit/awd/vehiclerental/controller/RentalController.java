package tn.esprit.awd.vehiclerental.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.awd.vehiclerental.dto.*;
import tn.esprit.awd.vehiclerental.service.RentalService;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
@Tag(name = "Locations", description = "Réservations de véhicules et états des lieux")
public class RentalController {

    private final RentalService service;

    public RentalController(RentalService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "Demander une réservation (202 : en attente de la caution)")
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationRequest r) {
        ReservationResponse created = service.create(r);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.accepted().location(location).body(created);
    }

    @GetMapping
    @Operation(summary = "Lister les réservations (filtre customerId optionnel)")
    public List<ReservationResponse> list(@RequestParam(required = false) Long customerId) { return service.list(customerId); }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter une réservation")
    public ReservationResponse get(@PathVariable Long id) { return service.get(id); }

    @PatchMapping("/{id}/confirmation")
    @Operation(summary = "Confirmer (simule la caution payée)")
    public ReservationResponse confirm(@PathVariable Long id) { return service.confirm(id); }

    @PatchMapping("/{id}/annulation")
    @Operation(summary = "Annuler une réservation")
    public ReservationResponse cancel(@PathVariable Long id) { return service.cancel(id); }

    @PostMapping("/{id}/inspections")
    @Operation(summary = "État des lieux : CHECK_IN au départ, CHECK_OUT au retour")
    public ResponseEntity<InspectionResponse> inspect(@PathVariable Long id, @Valid @RequestBody InspectionRequest r) {
        return ResponseEntity.status(201).body(service.inspect(id, r));
    }
}
