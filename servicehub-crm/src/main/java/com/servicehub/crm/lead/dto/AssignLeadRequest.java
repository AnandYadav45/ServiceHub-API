package com.servicehub.crm.lead.dto;

import jakarta.validation.constraints.NotNull;

public record AssignLeadRequest(@NotNull Integer vendorId, @NotNull Integer adminUserId) {
}
