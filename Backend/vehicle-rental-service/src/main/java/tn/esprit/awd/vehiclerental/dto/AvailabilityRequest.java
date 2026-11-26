package tn.esprit.awd.vehiclerental.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AvailabilityRequest(@NotNull LocalDate startDate, @NotNull LocalDate endDate,
                                  @NotNull Boolean isAvailable, String notes) {}
