package com.servicehub.commerce.product.dto;

import com.servicehub.commerce.product.enums.ProductCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductSaveRequest(
        Long id,
        @NotNull Long vendorId,
        @NotBlank String brand,
        @NotBlank String model,
        @NotNull ProductCondition condition,
        @NotNull @Positive BigDecimal price,
        @NotNull @PositiveOrZero Integer stockQuantity
) {}
