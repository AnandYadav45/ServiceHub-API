package com.servicehub.crm.catalog.dto;

import java.math.BigDecimal;

public record SubCategoryResponse(
        Long id,
        Long categoryId,
        String categoryName,
        String name,
        String slug,
        boolean requiresQuote,
        BigDecimal basePrice
) {
}
