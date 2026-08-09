package com.servicehub.crm.lead.dto;

import jakarta.validation.constraints.NotNull;

public record ServiceRequestSaveRequest(
        Long id,
        @NotNull Long subCategoryId,
        Long addressId,          // optional
        Long assignedVendorId,   // optional — present when an admin is assigning
        String description
) {
}
