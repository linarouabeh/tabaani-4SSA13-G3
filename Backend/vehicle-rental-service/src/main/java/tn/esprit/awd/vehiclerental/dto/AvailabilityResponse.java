package tn.esprit.awd.vehiclerental.dto;

import tn.esprit.awd.vehiclerental.entity.VehicleAvailability;
import java.time.LocalDate;

public record AvailabilityResponse(Long id, Long vehicleId, LocalDate startDate, LocalDate endDate,
                                   Boolean isAvailable, String notes) {
    public static AvailabilityResponse from(VehicleAvailability a) {
        return new AvailabilityResponse(a.getId(), a.getVehicle().getId(), a.getStartDate(), a.getEndDate(),
                a.getIsAvailable(), a.getNotes());
    }
}
