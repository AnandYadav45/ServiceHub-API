package com.servicehub.amc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AmcPlanSaveRequest(
        Long id,
        @NotBlank String name,
        @NotNull @Positive Integer durationMonths,
        @NotNull @Positive BigDecimal price,
        @NotNull @Positive Integer includedVisits
) {}
