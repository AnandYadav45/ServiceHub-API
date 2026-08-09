package com.servicehub.amc.dto;

import java.math.BigDecimal;

public record AmcPlanResponse(
        Long id,
        String name,
        Integer durationMonths,
        BigDecimal price,
        Integer includedVisits)
{}
