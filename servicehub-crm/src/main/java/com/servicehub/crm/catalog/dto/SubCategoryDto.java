package com.servicehub.crm.catalog.dto;

import java.math.BigDecimal;

public record SubCategoryDto(Long id, String name, boolean requiresQuote, BigDecimal basePrice) {
}
