package tn.esprit.awd.vehiclerental.dto;

import tn.esprit.awd.vehiclerental.entity.RentalReservation;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponse(Long id, Long vehicleId, Long customerId, LocalDateTime pickupDateTime,
                                  LocalDateTime returnDateTime, BigDecimal totalAmount, BigDecimal depositAmount,
                                  String status) {
    public static ReservationResponse from(RentalReservation r) {
        return new ReservationResponse(r.getId(), r.getVehicle().getId(), r.getCustomerId(), r.getPickupDateTime(),
                r.getReturnDateTime(), r.getTotalAmount(), r.getDepositAmount(), r.getStatus().name());
    }
}
