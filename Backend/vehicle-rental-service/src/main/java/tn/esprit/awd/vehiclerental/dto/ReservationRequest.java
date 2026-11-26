package tn.esprit.awd.vehiclerental.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ReservationRequest(@NotNull Long vehicleId, @NotNull Long customerId,
                                 @NotNull @Future LocalDateTime pickupDateTime,
                                 @NotNull @Future LocalDateTime returnDateTime) {}
