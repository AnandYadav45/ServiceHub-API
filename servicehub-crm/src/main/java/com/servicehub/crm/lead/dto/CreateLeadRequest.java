package com.servicehub.crm.lead.dto;

import jakarta.validation.constraints.NotNull;

public record CreateLeadRequest(@NotNull Integer customerId,
                                @NotNull Integer addressId,
                                @NotNull Integer subCategoryId,
                                String description) {
}
