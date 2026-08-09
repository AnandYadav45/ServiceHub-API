package com.servicehub.auth.dto;

import com.servicehub.common.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String fullName,
        @ValidPhoneNumber String contact,
        @NotBlank String email,
        @NotBlank @Size(min = 8, max = 100) String password
) {
}
