package com.servicehub.crm.lead.dto;

import java.math.BigDecimal;

public record ServiceRequestResponse(
        Long id,
        String requestCode,
        Long customerId,
        Long addressId,
        Long vendorId,
        String vendorName,
        Long subCategoryId,
        String subCategoryName,
        String status,
        String description,
        BigDecimal quotedPrice,
        BigDecimal finalPrice
) {
}
