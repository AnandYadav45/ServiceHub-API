package com.servicehub.auth.dto;

import com.servicehub.common.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @ValidPhoneNumber String contact,
        @NotBlank String password
) {}
