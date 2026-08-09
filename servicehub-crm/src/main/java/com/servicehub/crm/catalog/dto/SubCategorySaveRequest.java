package com.servicehub.crm.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SubCategorySaveRequest(
        Long id,
        @NotNull Long categoryId,   // the relation — a plain ID here, resolved to a real entity in the service
        @NotBlank String name,
        @NotBlank String slug,
        boolean requiresQuote,
        BigDecimal basePrice
) {
}
