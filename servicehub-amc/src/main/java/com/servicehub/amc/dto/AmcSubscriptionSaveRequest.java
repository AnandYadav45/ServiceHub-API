package com.servicehub.amc.dto;

import jakarta.validation.constraints.NotNull;

public record AmcSubscriptionSaveRequest(
        Long id,
        @NotNull Long amcPlanId
) {}
