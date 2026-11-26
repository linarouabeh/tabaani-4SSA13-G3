package tn.esprit.awd.vehiclerental.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.awd.vehiclerental.dto.*;
import tn.esprit.awd.vehiclerental.entity.*;
import tn.esprit.awd.vehiclerental.exception.*;
import tn.esprit.awd.vehiclerental.repository.*;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class VehicleService {

    static final Set<ReservationStatus> ACTIVE =
            EnumSet.of(ReservationStatus.EN_ATTENTE_CAUTION, ReservationStatus.CONFIRMEE, ReservationStatus.EN_COURS);

    private final VehicleRepository vehicles;
    private final VehicleAvailabilityRepository availabilities;
    private final RentalReservationRepository reservations;

    public VehicleService(VehicleRepository vehicles, VehicleAvailabilityRepository availabilities,
                          RentalReservationRepository reservations) {
        this.vehicles = vehicles;
        this.availabilities = availabilities;
        this.reservations = reservations;
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> search(String city, VehicleStatus status, LocalDateTime from, LocalDateTime to) {
        if ((from == null) != (to == null)) throw new BadRequestException("Both 'from' and 'to' are required together");
        List<Vehicle> result;
        if (from != null) {
            if (!to.isAfter(from)) throw new BadRequestException("'to' must be after 'from'");
            result = vehicles.findFreeBetween(status != null ? status : VehicleStatus.DISPONIBLE, ACTIVE, from, to);
            if (city != null) result = result.stream().filter(v -> v.getCity().equalsIgnoreCase(city)).toList();
        } else if (city != null && status != null) result = vehicles.findByCityIgnoreCaseAndStatus(city, status);
        else if (city != null) result = vehicles.findByCityIgnoreCase(city);
        else if (status != null) result = vehicles.findByStatus(status);
        else result = vehicles.findAll();
        return result.stream().map(VehicleResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public VehicleResponse get(Long id) { return VehicleResponse.from(find(id)); }

    public VehicleResponse create(VehicleRequest r) {
        if (vehicles.existsByRegistrationNumberIgnoreCase(r.registrationNumber()))
            throw new ConflictException("A vehicle with registration " + r.registrationNumber() + " already exists");
        Vehicle v = new Vehicle();
        apply(v, r);
        return VehicleResponse.from(vehicles.save(v));
    }

    public VehicleResponse update(Long id, VehicleRequest r) {
        Vehicle v = find(id);
        if (!v.getRegistrationNumber().equalsIgnoreCase(r.registrationNumber())
                && vehicles.existsByRegistrationNumberIgnoreCase(r.registrationNumber()))
            throw new ConflictException("A vehicle with registration " + r.registrationNumber() + " already exists");
        apply(v, r);
        return VehicleResponse.from(vehicles.save(v));
    }

    public VehicleResponse updateStatus(Long id, VehicleStatus status) {
        Vehicle v = find(id);
        v.setStatus(status);
        return VehicleResponse.from(v);
    }

    public void delete(Long id) {
        Vehicle v = find(id);
        if (reservations.existsByVehicleIdAndStatusIn(id, ACTIVE))
            throw new ConflictException("Vehicle " + id + " has active reservations; cancel them first");
        vehicles.delete(v);
    }

    // ---- disponibilités
    public AvailabilityResponse addAvailability(Long vehicleId, AvailabilityRequest r) {
        Vehicle v = find(vehicleId);
        if (r.endDate().isBefore(r.startDate())) throw new BadRequestException("endDate must not be before startDate");
        VehicleAvailability a = new VehicleAvailability();
        a.setVehicle(v);
        a.setStartDate(r.startDate());
        a.setEndDate(r.endDate());
        a.setIsAvailable(r.isAvailable());
        a.setNotes(r.notes());
        return AvailabilityResponse.from(availabilities.save(a));
    }

    @Transactional(readOnly = true)
    public List<AvailabilityResponse> listAvailabilities(Long vehicleId) {
        find(vehicleId);
        return availabilities.findByVehicleId(vehicleId).stream().map(AvailabilityResponse::from).toList();
    }

    private Vehicle find(Long id) {
        return vehicles.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vehicle with id " + id + " not found"));
    }

    private void apply(Vehicle v, VehicleRequest r) {
        v.setMake(r.make());
        v.setModel(r.model());
        v.setRegistrationNumber(r.registrationNumber());
        v.setBodyType(r.bodyType());
        v.setSeatingCapacity(r.seatingCapacity());
        v.setTransmissionType(r.transmissionType());
        v.setFuelType(r.fuelType());
        v.setCity(r.city());
        v.setPricePerDay(r.pricePerDay());
        v.setDepositAmount(r.depositAmount());
        v.setOwnerId(r.ownerId());
    }
}
