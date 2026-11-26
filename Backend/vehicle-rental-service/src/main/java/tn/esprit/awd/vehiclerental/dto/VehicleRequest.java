package tn.esprit.awd.vehiclerental.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record VehicleRequest(
        @NotBlank String make,
        @NotBlank String model,
        @NotBlank String registrationNumber,
        String bodyType,
        @NotNull @Min(1) Integer seatingCapacity,
        String transmissionType,
        String fuelType,
        @NotBlank String city,
        @NotNull @Positive BigDecimal pricePerDay,
        @PositiveOrZero BigDecimal depositAmount,
        @NotNull Long ownerId) {}
