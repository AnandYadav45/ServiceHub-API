package com.servicehub.commerce.product.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        Long vendorId,
        String brand,
        String model,
        String condition,
        BigDecimal price,
        Integer stockQuantity
) {}
