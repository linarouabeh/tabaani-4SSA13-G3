package tn.esprit.awd.vehiclerental.dto;

import tn.esprit.awd.vehiclerental.entity.Vehicle;
import java.math.BigDecimal;

public record VehicleResponse(Long id, String make, String model, String registrationNumber, String bodyType,
                              Integer seatingCapacity, String transmissionType, String fuelType, String city,
                              BigDecimal pricePerDay, BigDecimal depositAmount, Long ownerId, String status) {
    public static VehicleResponse from(Vehicle v) {
        return new VehicleResponse(v.getId(), v.getMake(), v.getModel(), v.getRegistrationNumber(), v.getBodyType(),
                v.getSeatingCapacity(), v.getTransmissionType(), v.getFuelType(), v.getCity(),
                v.getPricePerDay(), v.getDepositAmount(), v.getOwnerId(), v.getStatus().name());
    }
}
