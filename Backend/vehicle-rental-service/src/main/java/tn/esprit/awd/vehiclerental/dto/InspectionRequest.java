package tn.esprit.awd.vehiclerental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import tn.esprit.awd.vehiclerental.entity.InspectionResult;
import tn.esprit.awd.vehiclerental.entity.InspectionType;

public record InspectionRequest(@NotNull InspectionType inspectionType, @PositiveOrZero Integer odometerReading,
                                String observations, String damageReport, @NotNull InspectionResult inspectionResult) {}
