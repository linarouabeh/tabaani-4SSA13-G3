package tn.esprit.awd.vehiclerental.dto;

import jakarta.validation.constraints.NotNull;
import tn.esprit.awd.vehiclerental.entity.VehicleStatus;

public record VehicleStatusRequest(@NotNull VehicleStatus status) {}
