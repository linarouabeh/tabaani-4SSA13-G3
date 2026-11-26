package tn.esprit.awd.vehiclerental.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.awd.vehiclerental.dto.*;
import tn.esprit.awd.vehiclerental.entity.*;
import tn.esprit.awd.vehiclerental.exception.*;
import tn.esprit.awd.vehiclerental.repository.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RentalService {

    private final VehicleRepository vehicles;
    private final RentalReservationRepository reservations;
    private final VehicleAvailabilityRepository availabilities;
    private final VehicleInspectionRepository inspections;

    public RentalService(VehicleRepository vehicles, RentalReservationRepository reservations,
                         VehicleAvailabilityRepository availabilities, VehicleInspectionRepository inspections) {
        this.vehicles = vehicles;
        this.reservations = reservations;
        this.availabilities = availabilities;
        this.inspections = inspections;
    }

    public ReservationResponse create(ReservationRequest r) {
        Vehicle v = vehicles.findById(r.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with id " + r.vehicleId() + " not found"));
        if (!r.returnDateTime().isAfter(r.pickupDateTime()))
            throw new BadRequestException("returnDateTime must be after pickupDateTime");
        if (v.getStatus() != VehicleStatus.DISPONIBLE)
            throw new ConflictException("Vehicle " + v.getId() + " is not available (" + v.getStatus() + ")");
        if (availabilities.existsBlockedOverlap(v.getId(), r.pickupDateTime().toLocalDate(), r.returnDateTime().toLocalDate()))
            throw new ConflictException("Vehicle " + v.getId() + " is blocked by its owner on these dates");
        if (reservations.existsOverlap(v.getId(), VehicleService.ACTIVE, r.pickupDateTime(), r.returnDateTime()))
            throw new ConflictException("Vehicle " + v.getId() + " is already reserved on these dates");

        long hours = Duration.between(r.pickupDateTime(), r.returnDateTime()).toHours();
        long days = Math.max(1, (long) Math.ceil(hours / 24.0));

        RentalReservation res = new RentalReservation();
        res.setVehicle(v);
        res.setCustomerId(r.customerId());
        res.setPickupDateTime(r.pickupDateTime());
        res.setReturnDateTime(r.returnDateTime());
        res.setTotalAmount(v.getPricePerDay().multiply(BigDecimal.valueOf(days)));
        res.setDepositAmount(v.getDepositAmount());
        res.setStatus(ReservationStatus.EN_ATTENTE_CAUTION);
        return ReservationResponse.from(reservations.save(res));
    }

    @Transactional(readOnly = true)
    public ReservationResponse get(Long id) { return ReservationResponse.from(find(id)); }

    @Transactional(readOnly = true)
    public List<ReservationResponse> list(Long customerId) {
        List<RentalReservation> l = customerId != null ? reservations.findByCustomerId(customerId) : reservations.findAll();
        return l.stream().map(ReservationResponse::from).toList();
    }

    /** Simule la réception de VehicleDepositSucceeded (sera déclenché par RabbitMQ plus tard). */
    public ReservationResponse confirm(Long id) {
        RentalReservation res = find(id);
        if (res.getStatus() != ReservationStatus.EN_ATTENTE_CAUTION)
            throw new ConflictException("Reservation " + id + " cannot be confirmed from status " + res.getStatus());
        res.setStatus(ReservationStatus.CONFIRMEE);
        return ReservationResponse.from(res);
    }

    public ReservationResponse cancel(Long id) {
        RentalReservation res = find(id);
        if (res.getStatus() == ReservationStatus.TERMINEE || res.getStatus() == ReservationStatus.ANNULEE
                || res.getStatus() == ReservationStatus.EN_COURS)
            throw new ConflictException("Reservation " + id + " cannot be cancelled from status " + res.getStatus());
        res.setStatus(ReservationStatus.ANNULEE);
        return ReservationResponse.from(res);
    }

    /** CHECK_IN : CONFIRMEE -> EN_COURS ; CHECK_OUT : EN_COURS -> TERMINEE */
    public InspectionResponse inspect(Long id, InspectionRequest r) {
        RentalReservation res = find(id);
        if (r.inspectionType() == InspectionType.CHECK_IN && res.getStatus() != ReservationStatus.CONFIRMEE)
            throw new ConflictException("Check-in requires a CONFIRMEE reservation (current: " + res.getStatus() + ")");
        if (r.inspectionType() == InspectionType.CHECK_OUT && res.getStatus() != ReservationStatus.EN_COURS)
            throw new ConflictException("Check-out requires an EN_COURS reservation (current: " + res.getStatus() + ")");
        if (inspections.existsByReservationIdAndInspectionType(id, r.inspectionType()))
            throw new ConflictException("A " + r.inspectionType() + " inspection already exists for reservation " + id);

        VehicleInspection i = new VehicleInspection();
        i.setReservation(res);
        i.setInspectionType(r.inspectionType());
        i.setOdometerReading(r.odometerReading());
        i.setObservations(r.observations());
        i.setDamageReport(r.damageReport());
        i.setInspectionResult(r.inspectionResult());
        i.setInspectedAt(LocalDateTime.now());
        res.setStatus(r.inspectionType() == InspectionType.CHECK_IN ? ReservationStatus.EN_COURS : ReservationStatus.TERMINEE);
        return InspectionResponse.from(inspections.save(i));
    }

    private RentalReservation find(Long id) {
        return reservations.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + id + " not found"));
    }
}
