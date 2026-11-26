package tn.esprit.awd.vehiclerental.dto;

import tn.esprit.awd.vehiclerental.entity.VehicleInspection;
import java.time.LocalDateTime;

public record InspectionResponse(Long id, Long reservationId, String inspectionType, Integer odometerReading,
                                 String observations, String damageReport, String inspectionResult,
                                 LocalDateTime inspectedAt) {
    public static InspectionResponse from(VehicleInspection i) {
        return new InspectionResponse(i.getId(), i.getReservation().getId(), i.getInspectionType().name(),
                i.getOdometerReading(), i.getObservations(), i.getDamageReport(),
                i.getInspectionResult().name(), i.getInspectedAt());
    }
}
