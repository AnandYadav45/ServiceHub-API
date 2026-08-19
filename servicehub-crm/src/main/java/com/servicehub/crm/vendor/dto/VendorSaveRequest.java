package com.servicehub.crm.vendor.dto;

import com.servicehub.common.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

public record VendorSaveRequest(
        Long id,
        @NotBlank String fullName,
        @NotBlank String phoneNumber,
        @NotBlank String email,
        @NotBlank String passwordHash

) {}
